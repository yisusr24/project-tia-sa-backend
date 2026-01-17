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
public class VentaReporteDTO {
    private String numeroVenta;
    private String fecha;
    private BigDecimal total;
    private String metodoPago;
}
