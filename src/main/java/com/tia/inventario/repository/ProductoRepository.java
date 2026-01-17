package com.tia.inventario.repository;
import com.tia.inventario.constant.SqlQueries;
import com.tia.inventario.model.entity.Producto;
import com.tia.inventario.model.mapper.ProductoRowMapper;
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
public class ProductoRepository {
    private final JdbcTemplate jdbcTemplate;
    private final ProductoRowMapper productoRowMapper = new ProductoRowMapper();
    public List<Producto> findAll() {
        log.debug("Ejecutando: PRODUCTO_FIND_ALL");
        return jdbcTemplate.query(SqlQueries.PRODUCTO_FIND_ALL, productoRowMapper);
    }
    public List<Producto> findAllPaginated(int page, int size) {
        log.debug("Listando productos paginados - Page: {}, Size: {}", page, size);
        int offset = page * size;
        return jdbcTemplate.query(SqlQueries.PRODUCTO_FIND_ALL_PAGINATED, productoRowMapper, size, offset);
    }
    public long count() {
        return jdbcTemplate.queryForObject(SqlQueries.PRODUCTO_COUNT, Long.class);
    }
    public List<Producto> findDeletedPaginated(int page, int size) {
        log.debug("Listando eliminados paginados - Page: {}, Size: {}", page, size);
        int offset = page * size;
        return jdbcTemplate.query(SqlQueries.PRODUCTO_FIND_DELETED_PAGINATED, productoRowMapper, size, offset);
    }
    public long countDeleted() {
        return jdbcTemplate.queryForObject(SqlQueries.PRODUCTO_COUNT_DELETED, Long.class);
    }
    public Optional<Producto> findById(Long id) {
        log.debug("Buscando producto ID: {}", id);
        List<Producto> result = jdbcTemplate.query(SqlQueries.PRODUCTO_FIND_BY_ID, productoRowMapper, id);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }
    public Optional<Producto> findByCodigo(String codigo) {
        log.debug("Buscando producto por código: {}", codigo);
        List<Producto> result = jdbcTemplate.query(SqlQueries.PRODUCTO_FIND_BY_CODIGO, productoRowMapper, codigo);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }
    public List<Producto> buscarPorNombre(String query) {
        log.debug("Búsqueda parcial: {}", query);
        String term = "%" + query.trim() + "%";
        return jdbcTemplate.query(SqlQueries.PRODUCTO_SEARCH, productoRowMapper, term, term);
    }
    public Producto save(Producto producto) {
        String sql = """
            INSERT INTO productos (
                codigo, nombre, descripcion, categoria_id, proveedor_id, unidad_medida_id,
                precio_compra, precio_venta, precio_venta_minimo, stock_minimo, stock_maximo,
                imagen_url, es_perecedero, dias_vigencia, created_by
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, producto.getCodigo());
            ps.setString(2, producto.getNombre());
            ps.setString(3, producto.getDescripcion());
            ps.setObject(4, producto.getCategoriaId());
            ps.setObject(5, producto.getProveedorId());
            ps.setLong(6, producto.getUnidadMedidaId());
            ps.setBigDecimal(7, producto.getPrecioCompra());
            ps.setBigDecimal(8, producto.getPrecioVenta());
            ps.setBigDecimal(9, producto.getPrecioVentaMinimo());
            ps.setInt(10, producto.getStockMinimo() != null ? producto.getStockMinimo() : 10);
            ps.setObject(11, producto.getStockMaximo());
            ps.setString(12, producto.getImagenUrl());
            ps.setBoolean(13, producto.getEsPerecedero() != null ? producto.getEsPerecedero() : false);
            ps.setObject(14, producto.getDiasVigencia());
            ps.setString(15, producto.getCreatedBy());
            return ps;
        }, keyHolder);
        Long generatedId = (Long) keyHolder.getKeys().get("id");
        log.info("Producto creado con ID: {}", generatedId);
        return findById(generatedId).orElseThrow();
    }
    public Producto update(Producto producto) {
        String sql = """
            UPDATE productos SET
                nombre = ?, descripcion = ?, categoria_id = ?, proveedor_id = ?,
                precio_compra = ?, precio_venta = ?, precio_venta_minimo = ?,
                stock_minimo = ?, stock_maximo = ?, imagen_url = ?,
                es_perecedero = ?, dias_vigencia = ?, updated_by = ?
            WHERE id = ? AND deleted_at IS NULL
            """;
        int rowsAffected = jdbcTemplate.update(sql,
            producto.getNombre(),
            producto.getDescripcion(),
            producto.getCategoriaId(),
            producto.getProveedorId(),
            producto.getPrecioCompra(),
            producto.getPrecioVenta(),
            producto.getPrecioVentaMinimo(),
            producto.getStockMinimo(),
            producto.getStockMaximo(),
            producto.getImagenUrl(),
            producto.getEsPerecedero(),
            producto.getDiasVigencia(),
            producto.getUpdatedBy(),
            producto.getId()
        );
        log.info("Producto actualizado. Filas afectadas: {}", rowsAffected);
        return findById(producto.getId()).orElseThrow();
    }
    public void delete(Long id) {
        String sql = "UPDATE productos SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        log.info("Producto {} marcado como eliminado. Filas afectadas: {}", id, rowsAffected);
    }
    public List<Producto> findDeleted() {
        log.debug("Ejecutando: PRODUCTO_FIND_DELETED");
        return jdbcTemplate.query(SqlQueries.PRODUCTO_FIND_DELETED, productoRowMapper);
    }
    public void restore(Long id) {
        int rowsAffected = jdbcTemplate.update(SqlQueries.PRODUCTO_RESTORE, id);
        log.info("Producto {} restaurado. Filas afectadas: {}", id, rowsAffected);
    }
}