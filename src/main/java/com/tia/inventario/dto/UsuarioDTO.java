package com.tia.inventario.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String nombreUsuario;
    private String correo;
    private String nombre;
    private String apellido;
    private String telefono;
    private String rol;
    private Boolean activo;
    private Long localId;
    private String localNombre;
}