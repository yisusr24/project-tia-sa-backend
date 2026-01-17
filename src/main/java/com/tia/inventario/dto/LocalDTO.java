package com.tia.inventario.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class LocalDTO {
    @NotBlank(message = "El código es obligatorio")
    private String codigo;
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;
    private String ciudad;
    private String canton;
    private String pais;
    private String telefono;
    @Email(message = "El formato del correo es inválido")
    private String correo;
    private String tipo;
    private Boolean activo;
}