package com.tia.inventario.model.dto;
public class UsuarioDTO {
    private Long id;
    private String nombreUsuario;
    private String correo;
    private String nombre;
    private String apellido;
    private String telefono;
    private String rol;
    private Boolean activo;
    public UsuarioDTO() {}
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellido() {
        return apellido;
    }
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public String getRol() {
        return rol;
    }
    public void setRol(String rol) {
        this.rol = rol;
    }
    public Boolean getActivo() {
        return activo;
    }
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
    private Long localId;
    private String localNombre;
    public Long getLocalId() {
        return localId;
    }
    public void setLocalId(Long localId) {
        this.localId = localId;
    }
    public String getLocalNombre() {
        return localNombre;
    }
    public void setLocalNombre(String localNombre) {
        this.localNombre = localNombre;
    }
}