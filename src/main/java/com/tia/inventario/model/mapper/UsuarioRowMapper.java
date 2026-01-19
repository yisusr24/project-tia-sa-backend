package com.tia.inventario.model.mapper;

import com.tia.inventario.model.entity.Usuario;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioRowMapper implements RowMapper<Usuario> {
    @Override
    public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getLong("id"));
        usuario.setNombreUsuario(rs.getString("nombre_usuario"));
        usuario.setClave(rs.getString("clave"));
        usuario.setCorreo(rs.getString("correo"));
        usuario.setNombre(rs.getString("nombre"));
        usuario.setApellido(rs.getString("apellido"));
        usuario.setTelefono(rs.getString("telefono"));
        try {
            usuario.setRolNombre(rs.getString("rol_nombre"));
        } catch (SQLException e) {
            usuario.setRolNombre(null);
        }
        usuario.setActivo(rs.getBoolean("activo"));
        if (rs.getTimestamp("created_at") != null) {
            usuario.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }
        if (rs.getTimestamp("updated_at") != null) {
            usuario.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        }
        usuario.setCreatedBy(rs.getString("created_by"));
        usuario.setUpdatedBy(rs.getString("updated_by"));
        return usuario;
    }
}
