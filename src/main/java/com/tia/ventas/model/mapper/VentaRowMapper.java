package com.tia.ventas.model.mapper;

import com.tia.ventas.model.entity.Venta;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VentaRowMapper implements RowMapper<Venta> {
    @Override
    public Venta mapRow(ResultSet rs, int rowNum) throws SQLException {
        Venta venta = new Venta();
        venta.setId(rs.getLong("id"));
        venta.setNumeroVenta(rs.getString("numero_venta"));
        venta.setLocalId(rs.getLong("local_id"));
        venta.setVendedorId(rs.getLong("vendedor_id"));
        venta.setClienteNombre(rs.getString("cliente_nombre"));
        venta.setClienteDocumento(rs.getString("cliente_documento"));
        venta.setSubtotal(rs.getBigDecimal("subtotal"));
        venta.setImpuesto(rs.getBigDecimal("impuesto"));
        venta.setDescuento(rs.getBigDecimal("descuento"));
        venta.setTotal(rs.getBigDecimal("total"));
        venta.setMetodoPago(rs.getString("metodo_pago"));
        venta.setEstado(rs.getString("estado"));
        venta.setObservaciones(rs.getString("observaciones"));
        venta.setCreatedBy(rs.getString("created_by"));
        
        if (rs.getTimestamp("created_at") != null) {
            venta.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }

        try {
            venta.setLocalNombre(rs.getString("local_nombre"));
        } catch (SQLException ignored) {}

        return venta;
    }
}
