package com.tia.inventario.model.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusquedaProductoDTO {
    @NotBlank(message = "El término de búsqueda es obligatorio")
    private String query;
    private Integer limit;
}