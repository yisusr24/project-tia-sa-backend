package com.tia.inventario.repository;

import com.tia.inventario.model.entity.Proveedor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ProveedorRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Proveedor> rowMapper = (rs, rowNum) -> Proveedor.builder()
            .id(rs.getLong("id"))
            .ruc(rs.getString("ruc"))
            .razonSocial(rs.getString("razon_social"))
            .telefono(rs.getString("telefono"))
            .correo(rs.getString("correo"))
            .direccion(rs.getString("direccion"))
            .activo(rs.getBoolean("activo"))
            .build();

    public List<Proveedor> findAllActive() {
        String sql = "SELECT * FROM proveedores WHERE activo = true AND deleted_at IS NULL ORDER BY razon_social";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<Proveedor> findById(Long id) {
        String sql = "SELECT * FROM proveedores WHERE id = ? AND deleted_at IS NULL";
        List<Proveedor> results = jdbcTemplate.query(sql, rowMapper, id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }
}
