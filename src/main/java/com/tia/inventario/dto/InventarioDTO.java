package com.tia.inventario.dto;
import lombok.Data;
import java.time.LocalDate;
@Data
public class InventarioDTO {
    private Long id;
    private Long productoId;
    private Long localId;
    private Integer stockActual;
    private Integer stockMinimo;
    private Integer stockMaximo;
    private String ubicacion;
    private String lote;
    private LocalDate fechaVencimiento;
    private String productoNombre;
    private String productoCodigo;
    private String localNombre;
    private String unidadMedida;
    private java.math.BigDecimal precioVenta;
}