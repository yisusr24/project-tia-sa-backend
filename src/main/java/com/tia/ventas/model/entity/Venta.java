package com.tia.ventas.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Venta {
    private Long id;
    private String numeroVenta;
    private Long localId;
    private Long vendedorId;
    private String clienteNombre;
    private String clienteDocumento;
    private BigDecimal subtotal;
    private BigDecimal impuesto;
    private BigDecimal descuento;
    private BigDecimal total;
    private String metodoPago;
    private String estado;
    private String observaciones;
    private String createdBy;
    private LocalDateTime createdAt;
    
    private String localNombre;
    private String vendedorNombre;
    
    private List<DetalleVenta> items;
}
