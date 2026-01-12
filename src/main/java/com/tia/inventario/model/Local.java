package com.tia.inventario.model;
import lombok.Data;
import java.time.LocalDateTime;
@Data
public class Local {
    private Long id;
    private String codigo;
    private String nombre;
    private String direccion;
    private String ciudad;
    private String canton;
    private String pais;
    private String telefono;
    private String correo;
    private String tipo;
    private Boolean activo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime deletedAt;
}