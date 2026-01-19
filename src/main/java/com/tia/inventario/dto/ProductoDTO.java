package com.tia.inventario.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {
    @NotBlank(message = "El código es obligatorio")
    private String codigo;
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    private String descripcion;
    private Long categoriaId;
    private Long proveedorId;
    @NotNull(message = "La unidad de medida es obligatoria")
    private Long unidadMedidaId;
    @Positive(message = "El precio de compra debe ser mayor a 0")
    private BigDecimal precioCompra;
    @NotNull(message = "El precio de venta es obligatorio")
    @Positive(message = "El precio de venta debe ser mayor a 0")
    private BigDecimal precioVenta;
    private BigDecimal precioVentaMinimo;
    private Integer stockMinimo;
    private Integer stockMaximo;
    private String imagenUrl;
    private Boolean esPerecedero;
    private Integer diasVigencia;
}