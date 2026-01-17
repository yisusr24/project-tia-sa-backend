package com.tia.inventario.service;

import com.tia.inventario.config.ImportProperties;
import com.tia.inventario.dto.ImportErrorDTO;
import com.tia.inventario.dto.ImportResultDTO;
import com.tia.inventario.dto.ProductImportDTO;
import com.tia.inventario.model.entity.Producto;
import com.tia.inventario.repository.ProductoRepository;
import com.tia.inventario.util.DataParsingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductoImportService {

    private final ProductoRepository productoRepository;
    private final ImportProperties importProperties;
    private final CategoriaService categoriaService;
    private final UnidadMedidaService unidadMedidaService;

    public ImportResultDTO importProducts(MultipartFile file, String username) {
        Instant start = Instant.now();
        log.info("Iniciando importación masiva - Archivo: {} ({} KB) | Usuario: {}",
            file.getOriginalFilename(), 
            file.getSize() / 1024, 
            username);
        
        try {
            validateFile(file);
            
            List<ProductImportDTO> products = parseCSV(file);
            log.info("Archivo parseado: {} productos encontrados", products.size());
            
            log.info("Iniciando validación de datos...");
            List<ImportErrorDTO> validationErrors = validateProducts(products);
            
            if (!validationErrors.isEmpty()) {
                log.warn("Validación fallida: {} errores encontrados", validationErrors.size());
                return ImportResultDTO.builder()
                    .status("VALIDATION_FAILED")
                    .totalProcessed(products.size())
                    .successCount(0)
                    .errorCount(validationErrors.size())
                    .errors(validationErrors)
                    .message("Se encontraron errores de validación. Corrigelos antes de importar.")
                    .duration(Duration.between(start, Instant.now()).toMillis() + "ms")
                    .build();
            }
            
            log.info("Validación exitosa. Iniciando guardado en {} chunks de {}", 
                (products.size() / importProperties.getBatchSize()) + 1, importProperties.getBatchSize());
            
            int successCount = 0;
            List<ImportErrorDTO> saveErrors = new ArrayList<>();
            
            for (int i = 0; i < products.size(); i += importProperties.getBatchSize()) {
                List<ProductImportDTO> chunk = products.subList(
                    i, 
                    Math.min(i + importProperties.getBatchSize(), products.size())
                );
                
                try {
                    saveChunk(chunk, username);
                    successCount += chunk.size();
                } catch (Exception e) {
                    log.error("Error saving chunk starting en linea {}", i + 2, e);
                    saveErrors.add(ImportErrorDTO.builder()
                        .lineNumber(i + 2)
                        .errorMessage("Error al guardar lote: " + e.getMessage())
                        .build());
                }
            }
            
            String status = saveErrors.isEmpty() ? "SUCCESS" : "PARTIAL_SUCCESS";
            String message = saveErrors.isEmpty() 
                ? "Todos los productos fueron importados exitosamente"
                : "Algunos productos no pudieron ser guardados";
            
            long duration = Duration.between(start, Instant.now()).toMillis();
            
            if (saveErrors.isEmpty()) {
                log.info("Importación EXITOSA: {} productos en {}ms", successCount, duration);
            } else {
                log.warn("Importación PARCIAL: {} exitosos, {} errores en {}ms", 
                    successCount, saveErrors.size(), duration);
            }
            
            return ImportResultDTO.builder()
                .status(status)
                .totalProcessed(products.size())
                .successCount(successCount)
                .errorCount(saveErrors.size())
                .errors(saveErrors)
                .message(message)
                .duration(duration + "ms")
                .build();
                
        } catch (Exception e) {
            log.error("Error inesperado en importación", e);
            throw new RuntimeException("Error al procesar archivo: " + e.getMessage());
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacio");
        }
        
        if (file.getSize() > importProperties.getMaxFileSize()) {
            throw new RuntimeException("El archivo excede el tamaño máximo de " + 
                DataParsingUtils.formatFileSize(importProperties.getMaxFileSize()));
        }
        
        String filename = file.getOriginalFilename();
        if (filename == null || !filename.toLowerCase().endsWith(".csv")) {
            throw new RuntimeException("Solo se permiten archivos CSV");
        }
    }

    private List<ProductImportDTO> parseCSV(MultipartFile file) throws Exception {
        List<ProductImportDTO> products = new ArrayList<>();
        char delimiter = importProperties.getDelimiter();


        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String header = reader.readLine();
            if (header != null) {
                long commas = header.chars().filter(ch -> ch == ',').count();
                long semicolons = header.chars().filter(ch -> ch == ';').count();
                if (commas > semicolons) {
                    delimiter = ',';
                }
            }
        }
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), java.nio.charset.Charset.forName(importProperties.getEncoding())))) {
            
            CSVParser csvParser = CSVFormat.DEFAULT
                .withDelimiter(delimiter)
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim()
                .parse(reader);
            
            int lineNumber = 1;
            for (CSVRecord record : csvParser) {
                lineNumber++;
                
                if (lineNumber > importProperties.getMaxRecords() + 1) {
                    throw new RuntimeException("El archivo excede el maximo de " +
                        importProperties.getMaxRecords() + " registros");
                }
                
                try {
                    products.add(ProductImportDTO.builder()
                        .codigo(record.get("codigo"))
                        .nombre(record.get("nombre"))
                        .descripcion(record.get("descripcion"))
                        .categoriaId(DataParsingUtils.parseLong(record.get("categoria_id")))
                        .proveedorId(DataParsingUtils.parseLongWithDefault(record.get("proveedor_id"), null))
                        .unidadMedidaId(DataParsingUtils.parseLong(record.get("unidad_medida_id")))
                        .precioCompra(DataParsingUtils.parseBigDecimal(record.get("precio_compra")))
                        .precioVenta(DataParsingUtils.parseBigDecimal(record.get("precio_venta")))
                        .stockMinimo(DataParsingUtils.parseInteger(record.get("stock_minimo")))
                        .activo(DataParsingUtils.parseBoolean(record.get("activo")))
                        .build());
                } catch (IllegalArgumentException e) {
                    String detectedHeaders = csvParser.getHeaderNames().toString();
                    throw new RuntimeException("El archivo no tiene el formato correcto. " + translateCsvError(e.getMessage()) + 
                        ". Columnas detectadas: " + detectedHeaders + 
                        ". (Verifique que el separador sea ';')");
                }
            }
        }
        
        return products;
    }

    private List<ImportErrorDTO> validateProducts(List<ProductImportDTO> products) {
        List<ImportErrorDTO> errors = new ArrayList<>();
        Set<String> codesInFile = new HashSet<>();
        
        Set<Long> validCategories = new HashSet<>(categoriaService.getActiveCategoryIds());
        Set<Long> validUnits = new HashSet<>(unidadMedidaService.getActiveUnitIds());
        Set<Long> validProviders = new HashSet<>(productoRepository.findAllActiveProviderIds());
        
        for (int i = 0; i < products.size(); i++) {
            ProductImportDTO product = products.get(i);
            int lineNumber = i + 2;
            
            if (product.getCodigo() == null || product.getCodigo().trim().isEmpty()) {
                errors.add(ImportErrorDTO.builder()
                    .lineNumber(lineNumber)
                    .productCode(product.getCodigo())
                    .errorMessage("Código es requerido")
                    .build());
            } else {
                if (codesInFile.contains(product.getCodigo())) {
                    errors.add(ImportErrorDTO.builder()
                        .lineNumber(lineNumber)
                        .productCode(product.getCodigo())
                        .errorMessage("Código duplicado en el archivo")
                        .build());
                }
                
                codesInFile.add(product.getCodigo());
            }
            
            if (product.getNombre() == null || product.getNombre().trim().isEmpty()) {
                errors.add(ImportErrorDTO.builder()
                    .lineNumber(lineNumber)
                    .productCode(product.getCodigo())
                    .errorMessage("Nombre es requerido")
                    .build());
            }
            
            if (product.getCategoriaId() != null) {
                if (!validCategories.contains(product.getCategoriaId())) {
                    errors.add(ImportErrorDTO.builder()
                        .lineNumber(lineNumber)
                        .productCode(product.getCodigo())
                        .errorMessage("La categoría con ID " + product.getCategoriaId() + " no existe o está inactiva")
                        .build());
                }
            }

            if (product.getProveedorId() != null) {
                if (!validProviders.contains(product.getProveedorId())) {
                    errors.add(ImportErrorDTO.builder()
                        .lineNumber(lineNumber)
                        .productCode(product.getCodigo())
                        .errorMessage("El proveedor con ID " + product.getProveedorId() + " no existe o está inactivo")
                        .build());
                }
            }
            
            if (product.getUnidadMedidaId() == null) {
                errors.add(ImportErrorDTO.builder()
                    .lineNumber(lineNumber)
                    .productCode(product.getCodigo())
                    .errorMessage("Unidad de medida es requerida")
                    .build());
            } else {
                if (!validUnits.contains(product.getUnidadMedidaId())) {
                    errors.add(ImportErrorDTO.builder()
                        .lineNumber(lineNumber)
                        .productCode(product.getCodigo())
                        .errorMessage("La unidad de medida con ID " + product.getUnidadMedidaId() + " no existe")
                        .build());
                }
            }
            
            if (product.getPrecioVenta() != null && product.getPrecioVenta().compareTo(BigDecimal.ZERO) <= 0) {
                errors.add(ImportErrorDTO.builder()
                    .lineNumber(lineNumber)
                    .productCode(product.getCodigo())
                    .errorMessage("Precio de venta debe ser mayor a 0")
                    .build());
            }
        }
        
        return errors;
    }

    @Transactional
    protected void saveChunk(List<ProductImportDTO> chunk, String username) {
        List<String> codigos = chunk.stream()
            .map(ProductImportDTO::getCodigo)
            .collect(Collectors.toList());
            
        List<Producto> existentes = productoRepository.findByCodigoIn(codigos);
        Set<String> codigosExistentes = existentes.stream()
            .map(Producto::getCodigo)
            .collect(Collectors.toSet());
            
        List<Producto> toInsert = new ArrayList<>();
        List<Producto> toUpdate = new ArrayList<>();
        
        for (ProductImportDTO dto : chunk) {
            Producto producto = new Producto();
            producto.setCodigo(dto.getCodigo());
            producto.setNombre(dto.getNombre());
            producto.setDescripcion(dto.getDescripcion());
            producto.setCategoriaId(dto.getCategoriaId());
            producto.setProveedorId(dto.getProveedorId());
            producto.setUnidadMedidaId(dto.getUnidadMedidaId());
            producto.setPrecioCompra(dto.getPrecioCompra());
            producto.setPrecioVenta(dto.getPrecioVenta());
            producto.setStockMinimo(dto.getStockMinimo() != null ? dto.getStockMinimo() : 0);
            producto.setActivo(dto.getActivo() != null ? dto.getActivo() : true);
            
            if (codigosExistentes.contains(dto.getCodigo())) {
                producto.setUpdatedBy(username);
                toUpdate.add(producto);
            } else {
                producto.setCreatedBy(username);
                toInsert.add(producto);
            }
        }
        
        if (!toInsert.isEmpty()) {
            productoRepository.saveAll(toInsert);
            log.debug("Insertados {} productos", toInsert.size());
        }
        
        if (!toUpdate.isEmpty()) {
            productoRepository.updateAll(toUpdate);
            log.debug("Actualizados {} productos", toUpdate.size());
        }
    }

    public String generateTemplate() {
        String d = String.valueOf(importProperties.getDelimiter());
        
        String header = String.join(d, 
            "codigo", "nombre", "descripcion", "categoria_id", "proveedor_id", 
            "unidad_medida_id", "precio_compra", "precio_venta", 
            "stock_minimo", "activo");
            
        String row1 = String.join(d, 
            "PROD001", "Laptop HP 15", "Laptop empresarial 8GB RAM", 
            "1", "1", "1", "800.00", "1200.00", "5", "true");
            
        String row2 = String.join(d, 
            "PROD002", "Mouse Logitech M185", "Mouse inalámbrico USB", 
            "2", "1", "1", "15.00", "25.00", "20", "true");
            
        String row3 = String.join(d, 
            "PROD003", "Teclado Mecánico RGB", "Teclado retroiluminado", 
            "2", "1", "1", "45.00", "75.00", "10", "true");
            
        return header + "\n" + row1 + "\n" + row2 + "\n" + row3;
    }

    private String translateCsvError(String technicalMessage) {
        if (technicalMessage != null && technicalMessage.contains("Mapping for") && technicalMessage.contains("not found")) {

            int start = technicalMessage.indexOf("Mapping for") + 12;
            int end = technicalMessage.indexOf("not found");
            if (start > 0 && end > start) {
                String column = technicalMessage.substring(start, end).trim();
                return "Falta la columna: " + column;
            }
            return "Faltan columnas requeridas en la cabecera.";
        }
        return technicalMessage;
    }
}
