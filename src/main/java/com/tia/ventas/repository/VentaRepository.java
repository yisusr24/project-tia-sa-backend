package com.tia.ventas.repository;
import com.tia.ventas.dto.VentaDTO;
import com.tia.ventas.dto.DetalleVentaDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
@Repository
@RequiredArgsConstructor
@Slf4j
public class VentaRepository {
    private final JdbcTemplate jdbcTemplate;
    public VentaDTO create(VentaDTO venta, String username) {
        String sql = "INSERT INTO ventas (local_id, vendedor_id, cliente_nombre, cliente_documento, " +
                "subtotal, impuesto, descuento, total, metodo_pago, estado, observaciones, created_by) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, venta.getLocalId());
            ps.setLong(2, venta.getVendedorId());
            ps.setString(3, venta.getClienteNombre());
            ps.setString(4, venta.getClienteDocumento());
            ps.setBigDecimal(5, venta.getSubtotal());
            ps.setBigDecimal(6, venta.getImpuesto());
            ps.setBigDecimal(7, venta.getDescuento());
            ps.setBigDecimal(8, venta.getTotal());
            ps.setString(9, venta.getMetodoPago());
            ps.setString(10, venta.getEstado() != null ? venta.getEstado() : "COMPLETADA");
            ps.setString(11, venta.getObservaciones());
            ps.setString(12, username);
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
        for (DetalleVentaDTO item : venta.getItems()) {
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
        String sqlSelect = "SELECT numero_venta, created_at FROM ventas WHERE id = ?";
        jdbcTemplate.query(sqlSelect, rs -> {
            venta.setNumeroVenta(rs.getString("numero_venta"));
            venta.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }, ventaId);
        return venta;
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
    public List<VentaDTO> findAllPaginated(int page, int size) {
        int offset = page * size;
        String sql = """
            SELECT v.*, l.nombre as local_nombre
            FROM ventas v
            JOIN locales l ON v.local_id = l.id
            ORDER BY v.created_at DESC
            LIMIT ? OFFSET ?
        """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            VentaDTO dto = new VentaDTO();
            dto.setId(rs.getLong("id"));
            dto.setNumeroVenta(rs.getString("numero_venta"));
            dto.setLocalId(rs.getLong("local_id"));
            dto.setClienteNombre(rs.getString("cliente_nombre"));
            dto.setClienteDocumento(rs.getString("cliente_documento"));
            dto.setTotal(rs.getBigDecimal("total"));
            dto.setEstado(rs.getString("estado"));
            dto.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            return dto;
        }, size, offset);
    }
    public java.util.Optional<VentaDTO> findById(Long id) {
        String sql = "SELECT * FROM ventas WHERE id = ?";
        List<VentaDTO> results = jdbcTemplate.query(sql, (rs, rowNum) -> {
            VentaDTO dto = new VentaDTO();
            dto.setId(rs.getLong("id"));
            dto.setNumeroVenta(rs.getString("numero_venta"));
            dto.setLocalId(rs.getLong("local_id"));
            dto.setClienteNombre(rs.getString("cliente_nombre"));
            dto.setClienteDocumento(rs.getString("cliente_documento"));
            dto.setSubtotal(rs.getBigDecimal("subtotal"));
            dto.setImpuesto(rs.getBigDecimal("impuesto"));
            dto.setTotal(rs.getBigDecimal("total"));
            dto.setEstado(rs.getString("estado"));
            dto.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            return dto;
        }, id);
        if (results.isEmpty()) {
            return java.util.Optional.empty();
        }
        VentaDTO venta = results.get(0);
        String sqlItems = """
            SELECT d.*, p.nombre as producto_nombre, p.codigo as producto_codigo
            FROM detalle_ventas d
            JOIN productos p ON d.producto_id = p.id
            WHERE d.venta_id = ?
        """;
        List<DetalleVentaDTO> items = jdbcTemplate.query(sqlItems, (rs, rowNum) -> {
            DetalleVentaDTO item = new DetalleVentaDTO();
            item.setId(rs.getLong("id"));
            item.setProductoId(rs.getLong("producto_id"));
            item.setProductoNombre(rs.getString("producto_nombre"));
            item.setProductoCodigo(rs.getString("producto_codigo"));
            item.setCantidad(rs.getInt("cantidad"));
            item.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
            item.setSubtotal(rs.getBigDecimal("subtotal"));
            item.setTotal(rs.getBigDecimal("total"));
            return item;
        }, id);
        venta.setItems(items);
        return java.util.Optional.of(venta);
    }
}