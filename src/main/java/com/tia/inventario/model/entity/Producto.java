package com.tia.inventario.model.entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
    private Long id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private Long categoriaId;
    private Long proveedorId;
    private Long unidadMedidaId;
    private BigDecimal precioCompra;
    private BigDecimal precioVenta;
    private BigDecimal precioVentaMinimo;
    private Integer stockMinimo;
    private Integer stockMaximo;
    private String imagenUrl;
    private Boolean esPerecedero;
    private Integer diasVigencia;
    private Boolean activo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime deletedAt;
    private String categoriaNombre;
    private String proveedorNombre;
}