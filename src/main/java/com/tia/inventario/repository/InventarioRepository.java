package com.tia.inventario.repository;
import com.tia.inventario.model.Inventario;
import com.tia.inventario.model.MovimientoInventario;
import com.tia.inventario.model.mapper.InventarioRowMapper;
import com.tia.inventario.model.mapper.MovimientoInventarioRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
@Repository
public class InventarioRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final InventarioRowMapper inventarioRowMapper = new InventarioRowMapper();
    private final MovimientoInventarioRowMapper movimientoRowMapper = new MovimientoInventarioRowMapper();
    public List<Inventario> findByLocalId(Long localId) {
        String sql = """
            SELECT i.*, p.nombre as producto_nombre, p.codigo as producto_codigo, l.nombre as local_nombre, p.precio_venta
            FROM inventario i
            JOIN productos p ON i.producto_id = p.id
            JOIN locales l ON i.local_id = l.id
            WHERE i.local_id = ?
            ORDER BY p.nombre
        """;
        return jdbcTemplate.query(sql, inventarioRowMapper, localId);
    }
    public Optional<Inventario> findByLocalAndProducto(Long localId, Long productoId) {
        try {
            String sql = """
                SELECT i.*, p.nombre as producto_nombre, p.codigo as producto_codigo, l.nombre as local_nombre, p.precio_venta
                FROM inventario i
                JOIN productos p ON i.producto_id = p.id
                JOIN locales l ON i.local_id = l.id
                WHERE i.local_id = ? AND i.producto_id = ?
            """;
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, inventarioRowMapper, localId, productoId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    public void save(Inventario inventario) {
        if (inventario.getId() != null) {
            update(inventario);
        } else {
            insert(inventario);
        }
    }
    private void insert(Inventario i) {
        String sql = """
            INSERT INTO inventario (producto_id, local_id, stock_actual, stock_minimo, stock_maximo, created_by, updated_by)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        jdbcTemplate.update(sql, i.getProductoId(), i.getLocalId(), i.getStockActual(),
                i.getStockMinimo(), i.getStockMaximo(), i.getCreatedBy(), i.getUpdatedBy());
    }
    private void update(Inventario i) {
        String sql = """
            UPDATE inventario SET stock_actual = ?, updated_at = CURRENT_TIMESTAMP, updated_by = ?
            WHERE id = ?
        """;
        jdbcTemplate.update(sql, i.getStockActual(), i.getUpdatedBy(), i.getId());
    }
    public void saveMovimiento(MovimientoInventario m) {
        String sql = """
            INSERT INTO movimientos_inventario (
                producto_id, local_id, tipo_movimiento, cantidad, stock_anterior, stock_nuevo,
                precio_unitario, motivo, numero_documento, created_by
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;
        jdbcTemplate.update(sql, m.getProductoId(), m.getLocalId(), m.getTipoMovimiento(),
                m.getCantidad(), m.getStockAnterior(), m.getStockNuevo(), m.getPrecioUnitario(),
                m.getMotivo(), m.getNumeroDocumento(), m.getCreatedBy());
    }
    public long countByLocalId(Long localId) {
        String sql = "SELECT COUNT(*) FROM inventario WHERE local_id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, Long.class, localId)).orElse(0L);
    }
    public long countLowStockByLocalId(Long localId) {
        String sql = "SELECT COUNT(*) FROM inventario WHERE local_id = ? AND stock_actual <= CASE WHEN stock_minimo > 0 THEN stock_minimo ELSE 5 END";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, Long.class, localId)).orElse(0L);
    }
}