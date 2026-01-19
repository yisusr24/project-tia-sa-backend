package com.tia.inventario.repository;
import com.tia.inventario.model.entity.Usuario;
import com.tia.inventario.model.mapper.UsuarioRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
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
    private final UsuarioRowMapper usuarioRowMapper = new UsuarioRowMapper();

    public Usuario findByNombreUsuario(String nombreUsuario) {
        String sql = """
            SELECT u.*, r.nombre as rol_nombre
            FROM usuarios u
            LEFT JOIN usuarios_roles ur ON u.id = ur.usuario_id
            LEFT JOIN roles r ON ur.rol_id = r.id
            WHERE u.nombre_usuario = ? AND u.activo = true
            LIMIT 1
        """;
        List<Usuario> usuarios = jdbcTemplate.query(sql, usuarioRowMapper, nombreUsuario);
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
        List<Usuario> usuarios = jdbcTemplate.query(sql, usuarioRowMapper, id);
        return usuarios.isEmpty() ? null : usuarios.get(0);
    }
}