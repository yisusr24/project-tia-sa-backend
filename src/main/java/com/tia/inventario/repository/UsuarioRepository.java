package com.tia.inventario.repository;
import com.tia.inventario.model.entity.Usuario;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
@Repository
public class UsuarioRepository {
    private final JdbcTemplate jdbcTemplate;
    public UsuarioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    private static final class UsuarioRowMapper implements RowMapper<Usuario> {
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
    public Usuario findByNombreUsuario(String nombreUsuario) {
        String sql = """
            SELECT u.*, r.nombre as rol_nombre
            FROM usuarios u
            LEFT JOIN usuarios_roles ur ON u.id = ur.usuario_id
            LEFT JOIN roles r ON ur.rol_id = r.id
            WHERE u.nombre_usuario = ? AND u.activo = true
            LIMIT 1
        """;
        List<Usuario> usuarios = jdbcTemplate.query(sql, new UsuarioRowMapper(), nombreUsuario);
        return usuarios.isEmpty() ? null : usuarios.get(0);
    }
    public Usuario findById(Long id) {
        String sql = """
            SELECT u.*, r.nombre as rol_nombre
            FROM usuarios u
            LEFT JOIN usuarios_roles ur ON u.id = ur.usuario_id
            LEFT JOIN roles r ON ur.rol_id = r.id
            WHERE u.id = ?
            LIMIT 1
        """;
        List<Usuario> usuarios = jdbcTemplate.query(sql, new UsuarioRowMapper(), id);
        return usuarios.isEmpty() ? null : usuarios.get(0);
    }
}