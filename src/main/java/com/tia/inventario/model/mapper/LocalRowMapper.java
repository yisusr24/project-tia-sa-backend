package com.tia.inventario.model.mapper;
import com.tia.inventario.model.Local;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
public class LocalRowMapper implements RowMapper<Local> {
    @Override
    public Local mapRow(ResultSet rs, int rowNum) throws SQLException {
        Local local = new Local();
        local.setId(rs.getLong("id"));
        local.setCodigo(rs.getString("codigo"));
        local.setNombre(rs.getString("nombre"));
        local.setDireccion(rs.getString("direccion"));
        local.setCiudad(rs.getString("ciudad"));
        local.setCanton(rs.getString("canton"));
        local.setPais(rs.getString("pais"));
        local.setTelefono(rs.getString("telefono"));
        local.setCorreo(rs.getString("correo"));
        local.setTipo(rs.getString("tipo"));
        local.setActivo(rs.getObject("activo", Boolean.class));
        local.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
        local.setUpdatedAt(rs.getObject("updated_at", LocalDateTime.class));
        local.setCreatedBy(rs.getString("created_by"));
        local.setUpdatedBy(rs.getString("updated_by"));
        local.setDeletedAt(rs.getObject("deleted_at", LocalDateTime.class));
        return local;
    }
}