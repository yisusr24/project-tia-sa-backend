package com.tia.inventario.model;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
public class Inventario {
    private Long id;
    private Long productoId;
    private Long localId;
    private Integer stockActual;
    private Integer stockMinimo;
    private Integer stockMaximo;
    private String ubicacion;
    private String lote;
    private LocalDate fechaVencimiento;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private String productoNombre;
    private String productoCodigo;
    private String localNombre;
    private java.math.BigDecimal precioVenta;
}