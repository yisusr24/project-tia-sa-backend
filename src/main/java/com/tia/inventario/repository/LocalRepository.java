package com.tia.inventario.repository;
import com.tia.inventario.constant.SqlQueries;
import com.tia.inventario.model.Local;
import com.tia.inventario.model.mapper.LocalRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
@Slf4j
@Repository
@RequiredArgsConstructor
public class LocalRepository {
    private final JdbcTemplate jdbcTemplate;
    private final LocalRowMapper localRowMapper = new LocalRowMapper();
    public List<Local> findAll() {
        return jdbcTemplate.query(SqlQueries.LOCAL_FIND_ALL, localRowMapper);
    }
    public Optional<Local> findById(Long id) {
        List<Local> result = jdbcTemplate.query(SqlQueries.LOCAL_FIND_BY_ID, localRowMapper, id);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }
    public Local save(Local local) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SqlQueries.LOCAL_INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, local.getCodigo());
            ps.setString(2, local.getNombre());
            ps.setString(3, local.getDireccion());
            ps.setString(4, local.getCiudad());
            ps.setString(5, local.getCanton());
            ps.setString(6, local.getPais());
            ps.setString(7, local.getTelefono());
            ps.setString(8, local.getCorreo());
            ps.setString(9, local.getTipo());
            ps.setObject(10, local.getActivo() != null ? local.getActivo() : true);
            ps.setString(11, local.getCreatedBy());
            return ps;
        }, keyHolder);
        Long generatedId = (Long) keyHolder.getKeys().get("id");
        log.info("Local creado con ID: {}", generatedId);
        return findById(generatedId).orElseThrow();
    }
    public Local update(Local local) {
        int rowsAffected = jdbcTemplate.update(SqlQueries.LOCAL_UPDATE,
            local.getNombre(),
            local.getDireccion(),
            local.getCiudad(),
            local.getCanton(),
            local.getPais(),
            local.getTelefono(),
            local.getCorreo(),
            local.getTipo(),
            local.getActivo(),
            local.getUpdatedBy(),
            local.getId()
        );
        log.info("Local actualizado. Filas afectadas: {}", rowsAffected);
        return findById(local.getId()).orElseThrow();
    }
    public void delete(Long id) {
        jdbcTemplate.update(SqlQueries.LOCAL_DELETE, id);
        log.info("Local eliminado (soft delete) ID: {}", id);
    }
    public List<Local> findDeleted() {
        return jdbcTemplate.query(SqlQueries.LOCAL_FIND_DELETED, localRowMapper);
    }
    public void restore(Long id) {
        jdbcTemplate.update(SqlQueries.LOCAL_RESTORE, id);
        log.info("Local restaurado ID: {}", id);
    }
}