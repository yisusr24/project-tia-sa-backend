package com.tia.inventario.model.mapper;
import com.tia.inventario.model.MovimientoInventario;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
public class MovimientoInventarioRowMapper implements RowMapper<MovimientoInventario> {
    @Override
    public MovimientoInventario mapRow(ResultSet rs, int rowNum) throws SQLException {
        MovimientoInventario m = new MovimientoInventario();
        m.setId(rs.getLong("id"));
        m.setProductoId(rs.getLong("producto_id"));
        m.setLocalId(rs.getLong("local_id"));
        m.setTipoMovimiento(rs.getString("tipo_movimiento"));
        m.setCantidad(rs.getInt("cantidad"));
        m.setStockAnterior(rs.getInt("stock_anterior"));
        m.setStockNuevo(rs.getInt("stock_nuevo"));
        m.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
        m.setCostoTotal(rs.getBigDecimal("costo_total"));
        m.setMotivo(rs.getString("motivo"));
        m.setNumeroDocumento(rs.getString("numero_documento"));
        m.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        m.setCreatedBy(rs.getString("created_by"));
        try {
            m.setProductoNombre(rs.getString("producto_nombre"));
            m.setLocalNombre(rs.getString("local_nombre"));
        } catch (SQLException e) {
        }
        return m;
    }
}