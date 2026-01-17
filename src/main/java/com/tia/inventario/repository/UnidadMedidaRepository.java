package com.tia.inventario.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UnidadMedidaRepository {
    private final JdbcTemplate jdbcTemplate;

    public List<Long> findAllActiveIds() {
        String sql = "SELECT id FROM unidades_medida WHERE activo = TRUE";
        return jdbcTemplate.queryForList(sql, Long.class);
    }
}
