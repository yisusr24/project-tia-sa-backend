package com.tia.inventario.repository;

import com.tia.inventario.model.entity.Producto;
import com.tia.inventario.model.mapper.ProductoRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
        String sql = """
            SELECT p.*, c.nombre as categoria_nombre, pr.razon_social as proveedor_nombre 
            FROM productos p
            LEFT JOIN categorias c ON p.categoria_id = c.id
            LEFT JOIN proveedores pr ON p.proveedor_id = pr.id
            WHERE p.deleted_at IS NULL
            ORDER BY p.id DESC
            """;
        return jdbcTemplate.query(sql, productoRowMapper);
    }
    public List<Producto> findAllPaginated(int page, int size) {
        int offset = page * size;
        String sql = """
            SELECT p.*, c.nombre as categoria_nombre, pr.razon_social as proveedor_nombre 
            FROM productos p
            LEFT JOIN categorias c ON p.categoria_id = c.id
            LEFT JOIN proveedores pr ON p.proveedor_id = pr.id
            WHERE p.deleted_at IS NULL
            ORDER BY p.id DESC
            LIMIT ? OFFSET ?
            """;
        return jdbcTemplate.query(sql, productoRowMapper, size, offset);
    }
    public long count() {
        String sql = "SELECT COUNT(*) FROM productos WHERE deleted_at IS NULL";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
    public List<Producto> findDeletedPaginated(int page, int size) {
        int offset = page * size;
        String sql = """
            SELECT * FROM productos
            WHERE deleted_at IS NOT NULL
            ORDER BY id DESC
            LIMIT ? OFFSET ?
            """;
        return jdbcTemplate.query(sql, productoRowMapper, size, offset);
    }
    public long countDeleted() {
        String sql = "SELECT COUNT(*) FROM productos WHERE deleted_at IS NOT NULL";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }
    public Optional<Producto> findById(Long id) {
        String sql = """
            SELECT p.*, c.nombre as categoria_nombre, pr.razon_social as proveedor_nombre 
            FROM productos p
            LEFT JOIN categorias c ON p.categoria_id = c.id
            LEFT JOIN proveedores pr ON p.proveedor_id = pr.id
            WHERE p.id = ? AND p.deleted_at IS NULL
            """;
        List<Producto> result = jdbcTemplate.query(sql, productoRowMapper, id);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }
    public Optional<Producto> findByCodigo(String codigo) {
        String sql = """
            SELECT p.*, c.nombre as categoria_nombre, pr.razon_social as proveedor_nombre 
            FROM productos p
            LEFT JOIN categorias c ON p.categoria_id = c.id
            LEFT JOIN proveedores pr ON p.proveedor_id = pr.id
            WHERE p.codigo = ? AND p.deleted_at IS NULL
            """;
        List<Producto> result = jdbcTemplate.query(sql, productoRowMapper, codigo);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }
    public List<Producto> buscarPorNombre(String query) {
        String term = "%" + query.trim() + "%";
        String sql = """
            SELECT p.*, c.nombre as categoria_nombre, pr.razon_social as proveedor_nombre 
            FROM productos p
            LEFT JOIN categorias c ON p.categoria_id = c.id
            LEFT JOIN proveedores pr ON p.proveedor_id = pr.id
            WHERE (LOWER(p.nombre) LIKE LOWER(?) OR LOWER(p.codigo) LIKE LOWER(?))
              AND p.deleted_at IS NULL
            ORDER BY p.nombre
            """;
        return jdbcTemplate.query(sql, productoRowMapper, term, term);
    }

    public List<Producto> buscarPorNombrePaginado(String query, int page, int size) {
        log.debug("Búsqueda parcial paginada: {} [Page: {}, Size: {}]", query, page, size);
        String term = "%" + query.trim() + "%";
        int offset = page * size;
        String sql = """
            SELECT p.*, c.nombre as categoria_nombre, pr.razon_social as proveedor_nombre 
            FROM productos p
            LEFT JOIN categorias c ON p.categoria_id = c.id
            LEFT JOIN proveedores pr ON p.proveedor_id = pr.id
            WHERE (LOWER(p.nombre) LIKE LOWER(?) OR LOWER(p.codigo) LIKE LOWER(?)) 
            AND p.deleted_at IS NULL 
            ORDER BY p.nombre ASC 
            LIMIT ? OFFSET ?
            """;
        return jdbcTemplate.query(sql, productoRowMapper, term, term, size, offset);
    }

    public long countBuscarPorNombre(String query) {
        String term = "%" + query.trim() + "%";
        String sql = """
            SELECT COUNT(*) FROM productos 
            WHERE (LOWER(nombre) LIKE LOWER(?) OR LOWER(codigo) LIKE LOWER(?)) 
            AND deleted_at IS NULL
            """;
        return jdbcTemplate.queryForObject(sql, Long.class, term, term);
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
        String sql = "SELECT * FROM productos WHERE deleted_at IS NOT NULL ORDER BY id DESC";
        return jdbcTemplate.query(sql, productoRowMapper);
    }
    public void restore(Long id) {
        String sql = "UPDATE productos SET deleted_at = NULL, activo = TRUE WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        log.info("Producto {} restaurado. Filas afectadas: {}", id, rowsAffected);
    }

    public List<Producto> findByCodigoIn(List<String> codigos) {
        if (codigos.isEmpty()) return List.of();
        String placeholders = String.join(",", java.util.Collections.nCopies(codigos.size(), "?"));
        String sql = String.format("SELECT * FROM productos WHERE codigo IN (%s) AND deleted_at IS NULL", placeholders);
        return jdbcTemplate.query(sql, productoRowMapper, codigos.toArray());
    }

    @Transactional
    public void updateAll(List<Producto> productos) {
        String sql = """
            UPDATE productos SET
                nombre = ?, descripcion = ?, categoria_id = ?, proveedor_id = ?, unidad_medida_id = ?, 
                precio_compra = ?, precio_venta = ?, stock_minimo = ?, 
                activo = ?, updated_by = ?, updated_at = CURRENT_TIMESTAMP
            WHERE codigo = ? AND deleted_at IS NULL
            """;

        jdbcTemplate.batchUpdate(sql, new org.springframework.jdbc.core.BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws java.sql.SQLException {
                Producto p = productos.get(i);
                ps.setString(1, p.getNombre());
                ps.setString(2, p.getDescripcion());
                ps.setObject(3, p.getCategoriaId());
                ps.setObject(4, p.getProveedorId());
                ps.setLong(5, p.getUnidadMedidaId());
                ps.setBigDecimal(6, p.getPrecioCompra());
                ps.setBigDecimal(7, p.getPrecioVenta());
                ps.setInt(8, p.getStockMinimo() != null ? p.getStockMinimo() : 0);
                ps.setBoolean(9, p.getActivo());
                ps.setString(10, p.getUpdatedBy());
                ps.setString(11, p.getCodigo());
            }

            @Override
            public int getBatchSize() {
                return productos.size();
            }
        });
        
        log.info("Batch update completado para {} productos", productos.size());
    }

    public List<Long> findAllActiveProviderIds() {
        return jdbcTemplate.queryForList("SELECT id FROM proveedores WHERE activo = true AND deleted_at IS NULL", Long.class);
    }

    @Transactional
    public void saveAll(List<Producto> productos) {
        String sql = """
            INSERT INTO productos (
                codigo, nombre, descripcion, categoria_id, proveedor_id, unidad_medida_id,
                precio_compra, precio_venta, precio_venta_minimo, stock_minimo, stock_maximo,
                imagen_url, es_perecedero, dias_vigencia, created_by
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        jdbcTemplate.batchUpdate(sql, new org.springframework.jdbc.core.BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws java.sql.SQLException {
                Producto producto = productos.get(i);
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
            }

            @Override
            public int getBatchSize() {
                return productos.size();
            }
        });
        
        log.info("Batch insert completado para {} productos", productos.size());
    }
}