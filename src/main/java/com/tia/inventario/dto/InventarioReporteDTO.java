package com.tia.inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventarioReporteDTO {
    private String nombreProducto;
    private String codigoProducto;
    private String nombreLocal;
    private Integer stockActual;
    private BigDecimal precioVenta;
    private BigDecimal valorTotal;
    private String categoria;
}
