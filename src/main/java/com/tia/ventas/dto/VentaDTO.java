package com.tia.ventas.dto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VentaDTO {
    private Long id;
    private String numeroVenta;
    private Long localId;
    private String localNombre;
    private Long vendedorId;
    private String vendedorNombre;
    private String clienteNombre;
    private String clienteDocumento;
    private BigDecimal subtotal;
    private BigDecimal impuesto;
    private BigDecimal descuento;
    private BigDecimal total;
    private String metodoPago;
    private String estado;
    private String observaciones;
    private List<DetalleVentaDTO> items;
    private LocalDateTime createdAt;
}