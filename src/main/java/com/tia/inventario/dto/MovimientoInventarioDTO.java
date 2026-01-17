package com.tia.inventario.dto;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
public class MovimientoInventarioDTO {
    private Long id;
    private Long productoId;
    private Long localId;
    private String tipoMovimiento;
    private Integer cantidad;
    private Integer stockAnterior;
    private Integer stockNuevo;
    private BigDecimal precioUnitario;
    private String motivo;
    private Long localDestinoId;
    private String numeroDocumento;
    private LocalDateTime createdAt;
    private String createdBy;
    private String productoNombre;
}