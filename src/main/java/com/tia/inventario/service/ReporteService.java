package com.tia.inventario.service;

import com.tia.inventario.dto.InventarioDTO;
import com.tia.inventario.dto.InventarioReporteDTO;
import com.tia.inventario.dto.VentaReporteDTO;
import com.tia.inventario.model.entity.Inventario;
import com.tia.inventario.repository.InventarioRepository;
import com.tia.ventas.model.entity.Venta;
import com.tia.ventas.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReporteService {

    private final VentaRepository ventaRepository;
    private final InventarioRepository inventarioRepository;
    private final JasperReportService jasperService;
    
    private final DateTimeFormatter reporteFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public byte[] generarReporteVentas(String fechaInicio, String fechaFin, String formato, String username) {
        log.info("Generando reporte de ventas - Intervalo: {} - {}", fechaInicio, fechaFin);
        
        LocalDate inicio = LocalDate.parse(fechaInicio);
        LocalDate fin = LocalDate.parse(fechaFin);
        
        if (inicio.isAfter(fin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha fin");
        }
        
        List<Venta> ventas = ventaRepository.findByDateRange(inicio.atStartOfDay(), fin.atTime(23, 59, 59));
        
        if (ventas.isEmpty()) {
            throw new RuntimeException("No hay ventas en el rango especificado");
        }

        List<VentaReporteDTO> reporteData = ventas.stream()
            .map(v -> VentaReporteDTO.builder()
                .numeroVenta(v.getNumeroVenta() != null ? v.getNumeroVenta() : String.valueOf(v.getId()))
                .fecha(v.getCreatedAt().format(reporteFormatter))
                .total(v.getTotal())
                .metodoPago(v.getMetodoPago() != null ? v.getMetodoPago() : "EFECTIVO")
                .build())
            .collect(Collectors.toList());
            
        Map<String, Object> params = new HashMap<>();
        params.put("fechaInicio", fechaInicio);
        params.put("fechaFin", fechaFin);
        params.put("usuario", username);
        
        return "excel".equalsIgnoreCase(formato)
            ? jasperService.generarReporteExcel("ventas_reporte", reporteData, params)
            : jasperService.generarReportePDF("ventas_reporte", reporteData, params);
    }
    
    public byte[] generarReporteInventario(Long localId, String formato, String username) {
        log.info("Generando reporte inventario - Local: {}", localId);
        
        List<Inventario> items = inventarioRepository.findByLocalId(localId);
        
        if (items.isEmpty()) {
            throw new RuntimeException("No hay productos en el inventario seleccionado");
        }

        List<InventarioReporteDTO> reporteData = items.stream()
            .map(i -> InventarioReporteDTO.builder()
                .nombreProducto(i.getProductoNombre())
                .codigoProducto(i.getProductoCodigo() != null ? i.getProductoCodigo() : "N/A")
                .nombreLocal(i.getLocalNombre())
                .stockActual(i.getStockActual())
                .precioVenta(i.getPrecioVenta() != null ? i.getPrecioVenta() : BigDecimal.ZERO)
                .valorTotal(calcularValorTotal(i.getStockActual(), i.getPrecioVenta()))
                .categoria("General")
                .build())
            .collect(Collectors.toList());
            
        Map<String, Object> params = new HashMap<>();
        params.put("nombreLocal", items.get(0).getLocalNombre());
        params.put("fechaGeneracion", LocalDate.now().toString());
        params.put("usuario", username);
        
        return "excel".equalsIgnoreCase(formato)
            ? jasperService.generarReporteExcel("inventario_reporte", reporteData, params)
            : jasperService.generarReportePDF("inventario_reporte", reporteData, params);
    }
    
    private BigDecimal calcularValorTotal(Integer stock, BigDecimal precio) {
        if (stock == null || precio == null) return BigDecimal.ZERO;
        return precio.multiply(BigDecimal.valueOf(stock));
    }
}
