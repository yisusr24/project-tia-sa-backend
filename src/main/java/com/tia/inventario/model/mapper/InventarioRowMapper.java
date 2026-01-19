package com.tia.inventario.model.mapper;
import com.tia.inventario.model.entity.Inventario;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
public class InventarioRowMapper implements RowMapper<Inventario> {
    @Override
    public Inventario mapRow(ResultSet rs, int rowNum) throws SQLException {
        Inventario i = new Inventario();
        i.setId(rs.getLong("id"));
        i.setProductoId(rs.getLong("producto_id"));
        i.setLocalId(rs.getLong("local_id"));
        i.setStockActual(rs.getInt("stock_actual"));
        i.setStockMinimo(rs.getObject("stock_minimo") != null ? rs.getInt("stock_minimo") : null);
        i.setStockMaximo(rs.getObject("stock_maximo") != null ? rs.getInt("stock_maximo") : null);
        i.setUbicacion(rs.getString("ubicacion"));
        i.setLote(rs.getString("lote"));
        java.sql.Date fechaVenc = rs.getDate("fecha_vencimiento");
        if (fechaVenc != null) {
            i.setFechaVencimiento(fechaVenc.toLocalDate());
        }
        i.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        i.setCreatedBy(rs.getString("created_by"));
        i.setUpdatedBy(rs.getString("updated_by"));
        try {
            i.setProductoNombre(rs.getString("producto_nombre"));
            i.setProductoCodigo(rs.getString("producto_codigo"));
            i.setLocalNombre(rs.getString("local_nombre"));
            i.setPrecioVenta(rs.getBigDecimal("precio_venta"));
        } catch (SQLException e) {
        }
        return i;
    }
}