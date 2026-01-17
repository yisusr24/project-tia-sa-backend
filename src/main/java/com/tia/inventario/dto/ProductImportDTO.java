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
public class ProductImportDTO {
    private String codigo;
    private String nombre;
    private String descripcion;
    private Long categoriaId;
    private Long proveedorId;
    private Long unidadMedidaId;
    private BigDecimal precioCompra;
    private BigDecimal precioVenta;
    private Integer stockMinimo;
    private Boolean activo;
}
