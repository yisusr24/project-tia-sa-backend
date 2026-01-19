package com.tia.ventas.model.mapper;

import com.tia.ventas.model.entity.DetalleVenta;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DetalleVentaRowMapper implements RowMapper<DetalleVenta> {
    @Override
    public DetalleVenta mapRow(ResultSet rs, int rowNum) throws SQLException {
        DetalleVenta detalle = new DetalleVenta();
        detalle.setId(rs.getLong("id"));
        detalle.setVentaId(rs.getLong("venta_id"));
        detalle.setProductoId(rs.getLong("producto_id"));
        detalle.setCantidad(rs.getInt("cantidad"));
        detalle.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
        detalle.setSubtotal(rs.getBigDecimal("subtotal"));
        detalle.setDescuento(rs.getBigDecimal("descuento"));
        detalle.setTotal(rs.getBigDecimal("total"));
        
        try {
            detalle.setProductoNombre(rs.getString("producto_nombre"));
        } catch (SQLException ignored) {}
        
        try {
            detalle.setProductoCodigo(rs.getString("producto_codigo"));
        } catch (SQLException ignored) {}

        return detalle;
    }
}
