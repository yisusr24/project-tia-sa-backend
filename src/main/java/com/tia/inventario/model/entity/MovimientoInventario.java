package com.tia.inventario.model.entity;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
public class MovimientoInventario {
    private Long id;
    private Long productoId;
    private Long localId;
    private String tipoMovimiento;
    private Integer cantidad;
    private Integer stockAnterior;
    private Integer stockNuevo;
    private BigDecimal precioUnitario;
    private BigDecimal costoTotal;
    private Long localDestinoId;
    private Long ventaId;
    private String motivo;
    private String numeroDocumento;
    private LocalDateTime createdAt;
    private String createdBy;
    private String productoNombre;
    private String localNombre;
    private String localDestinoNombre;
}