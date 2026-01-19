package com.tia.ventas.repository;

import com.tia.ventas.model.entity.Venta;
import com.tia.ventas.model.entity.DetalleVenta;
import com.tia.ventas.model.mapper.VentaRowMapper;
import com.tia.ventas.model.mapper.DetalleVentaRowMapper;
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

@Repository
@RequiredArgsConstructor
@Slf4j
public class VentaRepository {
    private final JdbcTemplate jdbcTemplate;
    private final VentaRowMapper ventaRowMapper = new VentaRowMapper();
    private final DetalleVentaRowMapper detalleVentaRowMapper = new DetalleVentaRowMapper();

    public Venta create(Venta venta) {
        String sql = "INSERT INTO ventas (local_id, vendedor_id, cliente_nombre, cliente_documento, " +
                "subtotal, impuesto, descuento, total, metodo_pago, estado, observaciones, created_by) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, venta.getLocalId());
            ps.setObject(2, venta.getVendedorId());
            ps.setString(3, venta.getClienteNombre());
            ps.setString(4, venta.getClienteDocumento());
            ps.setBigDecimal(5, venta.getSubtotal());
            ps.setBigDecimal(6, venta.getImpuesto());
            ps.setBigDecimal(7, venta.getDescuento());
            ps.setBigDecimal(8, venta.getTotal());
            ps.setString(9, venta.getMetodoPago());
            ps.setString(10, venta.getEstado() != null ? venta.getEstado() : "COMPLETADA");
            ps.setString(11, venta.getObservaciones());
            ps.setString(12, venta.getCreatedBy());
            return ps;
        }, keyHolder);

        Number key = (Number) keyHolder.getKeys().get("id");
        if (key == null) {
            throw new RuntimeException("Error al generar ID de venta");
        }
        Long ventaId = key.longValue();
        venta.setId(ventaId);

        String sqlDetalle = "INSERT INTO detalle_ventas (venta_id, producto_id, cantidad, precio_unitario, " +
                "subtotal, descuento, total) VALUES (?, ?, ?, ?, ?, ?, ?)";

        if (venta.getItems() != null) {
            for (DetalleVenta item : venta.getItems()) {
                item.setVentaId(ventaId); // Ensure linkage
                jdbcTemplate.update(sqlDetalle,
                        ventaId,
                        item.getProductoId(),
                        item.getCantidad(),
                        item.getPrecioUnitario(),
                        item.getSubtotal(),
                        item.getDescuento(),
                        item.getTotal()
                );
            }
        }

        return findById(ventaId).orElse(venta);
    }

    public java.math.BigDecimal sumAllVentasHoy() {
        String sql = """
            SELECT COALESCE(SUM(total), 0)
            FROM ventas
            WHERE CAST(created_at AS DATE) = CURRENT_DATE
            AND estado = 'COMPLETADA'
        """;
        return jdbcTemplate.queryForObject(sql, java.math.BigDecimal.class);
    }

    public long countAll() {
        String sql = "SELECT COUNT(*) FROM ventas";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    public List<Venta> findAllPaginated(int page, int size) {
        int offset = page * size;
        String sql = """
            SELECT v.*, l.nombre as local_nombre
            FROM ventas v
            JOIN locales l ON v.local_id = l.id
            ORDER BY v.created_at DESC
            LIMIT ? OFFSET ?
        """;
        return jdbcTemplate.query(sql, ventaRowMapper, size, offset);
    }

    public Optional<Venta> findById(Long id) {
        String sql = """
            SELECT v.*, l.nombre as local_nombre
            FROM ventas v
            JOIN locales l ON v.local_id = l.id
            WHERE v.id = ?
        """;
        
        List<Venta> results = jdbcTemplate.query(sql, ventaRowMapper, id);
        if (results.isEmpty()) {
            return Optional.empty();
        }

        Venta venta = results.get(0);
        
        String sqlItems = """
            SELECT d.*, p.nombre as producto_nombre, p.codigo as producto_codigo
            FROM detalle_ventas d
            JOIN productos p ON d.producto_id = p.id
            WHERE d.venta_id = ?
        """;
        List<DetalleVenta> items = jdbcTemplate.query(sqlItems, detalleVentaRowMapper, id);
        venta.setItems(items);
        
        return Optional.of(venta);
    }

    public List<Venta> findByDateRange(java.time.LocalDateTime start, java.time.LocalDateTime end) {
        String sql = """
            SELECT v.*, l.nombre as local_nombre
            FROM ventas v
            JOIN locales l ON v.local_id = l.id
            WHERE v.created_at BETWEEN ? AND ?
            ORDER BY v.created_at DESC
        """;
        return jdbcTemplate.query(sql, ventaRowMapper, start, end);
    }
}