package com.tia.ventas.service;

import com.tia.inventario.dto.PageResponse;
import com.tia.ventas.dto.DetalleVentaDTO;
import com.tia.ventas.dto.VentaDTO;
import com.tia.ventas.model.entity.DetalleVenta;
import com.tia.ventas.model.entity.Venta;
import com.tia.ventas.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VentaService {
    private final VentaRepository ventaRepository;

    @Transactional
    public VentaDTO registrarVenta(VentaDTO ventaDTO, String username) {
        log.info("Registrando venta para local: {}", ventaDTO.getLocalId());
        try {
            Venta ventaEntity = mapToEntity(ventaDTO);
            ventaEntity.setCreatedBy(username);
            
            Venta savedVenta = ventaRepository.create(ventaEntity);
            return mapToDTO(savedVenta);
        } catch (Exception e) {
            log.error("Error al registrar venta: ", e);
            if (e.getMessage() != null && e.getMessage().contains("Stock insuficiente")) {
                 throw new RuntimeException(e.getMessage());
            }
            throw new RuntimeException("Error en Base de Datos: " + e.getMessage(), e);
        }
    }

    public PageResponse<VentaDTO> listarVentasPaginadas(int page, int size) {
        long totalElements = ventaRepository.countAll();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        List<Venta> entities = ventaRepository.findAllPaginated(page, size);
        List<VentaDTO> data = entities.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return new PageResponse<>(data, totalElements, totalPages, page, size);
    }

    public VentaDTO obtenerPorId(Long id) {
        Venta entity = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada: " + id));
        return mapToDTO(entity);
    }
    
    public List<VentaDTO> findAll() {
        log.info("Obteniendo todas las ventas para reporte");
        return ventaRepository.findAllPaginated(0, 10000).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    private Venta mapToEntity(VentaDTO dto) {
        Venta entity = Venta.builder()
                .id(dto.getId())
                .numeroVenta(dto.getNumeroVenta())
                .localId(dto.getLocalId())
                .vendedorId(dto.getVendedorId())
                .clienteNombre(dto.getClienteNombre())
                .clienteDocumento(dto.getClienteDocumento())
                .subtotal(dto.getSubtotal())
                .impuesto(dto.getImpuesto())
                .descuento(dto.getDescuento())
                .total(dto.getTotal())
                .metodoPago(dto.getMetodoPago())
                .estado(dto.getEstado())
                .observaciones(dto.getObservaciones())
                .createdAt(dto.getCreatedAt())
                .build();

        if (dto.getItems() != null) {
            entity.setItems(dto.getItems().stream()
                    .map(this::mapDetalleToEntity)
                    .collect(Collectors.toList()));
        }
        return entity;
    }

    private VentaDTO mapToDTO(Venta entity) {
        VentaDTO dto = VentaDTO.builder()
                .id(entity.getId())
                .numeroVenta(entity.getNumeroVenta())
                .localId(entity.getLocalId())
                .localNombre(entity.getLocalNombre())
                .vendedorId(entity.getVendedorId())
                .vendedorNombre(entity.getVendedorNombre())
                .clienteNombre(entity.getClienteNombre())
                .clienteDocumento(entity.getClienteDocumento())
                .subtotal(entity.getSubtotal())
                .impuesto(entity.getImpuesto())
                .descuento(entity.getDescuento())
                .total(entity.getTotal())
                .metodoPago(entity.getMetodoPago())
                .estado(entity.getEstado())
                .observaciones(entity.getObservaciones())
                .createdAt(entity.getCreatedAt())
                .build();

        if (entity.getItems() != null) {
            dto.setItems(entity.getItems().stream()
                    .map(this::mapDetalleToDTO)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    private DetalleVenta mapDetalleToEntity(DetalleVentaDTO dto) {
        return DetalleVenta.builder()
                .id(dto.getId())
                .productoId(dto.getProductoId())
                .cantidad(dto.getCantidad())
                .precioUnitario(dto.getPrecioUnitario())
                .subtotal(dto.getSubtotal())
                .descuento(dto.getDescuento())
                .total(dto.getTotal())
                .build();
    }

    private DetalleVentaDTO mapDetalleToDTO(DetalleVenta entity) {
        return DetalleVentaDTO.builder()
                .id(entity.getId())
                .productoId(entity.getProductoId())
                .productoNombre(entity.getProductoNombre()) // Joined field
                .productoCodigo(entity.getProductoCodigo()) // Joined field
                .cantidad(entity.getCantidad())
                .precioUnitario(entity.getPrecioUnitario())
                .subtotal(entity.getSubtotal())
                .descuento(entity.getDescuento())
                .total(entity.getTotal())
                .build();
    }
}