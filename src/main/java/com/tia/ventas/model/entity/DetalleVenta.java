package com.tia.ventas.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetalleVenta {
    private Long id;
    private Long ventaId;
    private Long productoId;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;
    private BigDecimal descuento;
    private BigDecimal total;
    
    private String productoNombre;
    private String productoCodigo;
}
