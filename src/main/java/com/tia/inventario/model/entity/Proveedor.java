package com.tia.inventario.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Proveedor {
    private Long id;
    private String ruc;
    private String razonSocial;
    private String telefono;
    private String correo; 
    private String direccion;
    private Boolean activo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime deletedAt;
}
