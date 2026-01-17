package com.tia.inventario.model.mapper;
import com.tia.inventario.model.entity.Producto;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
public class ProductoRowMapper implements RowMapper<Producto> {
    @Override
    public Producto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Producto.builder()
                .id(rs.getLong("id"))
                .codigo(rs.getString("codigo"))
                .nombre(rs.getString("nombre"))
                .descripcion(rs.getString("descripcion"))
                .categoriaId(rs.getLong("categoria_id"))
                .proveedorId(rs.getLong("proveedor_id"))
                .unidadMedidaId(rs.getLong("unidad_medida_id"))
                .precioCompra(rs.getBigDecimal("precio_compra"))
                .precioVenta(rs.getBigDecimal("precio_venta"))
                .precioVentaMinimo(rs.getBigDecimal("precio_venta_minimo"))
                .stockMinimo(rs.getInt("stock_minimo"))
                .stockMaximo((Integer) rs.getObject("stock_maximo"))
                .imagenUrl(rs.getString("imagen_url"))
                .esPerecedero(rs.getBoolean("es_perecedero"))
                .diasVigencia((Integer) rs.getObject("dias_vigencia"))
                .activo(rs.getBoolean("activo"))
                .createdAt(rs.getTimestamp("created_at") != null ?
                        rs.getTimestamp("created_at").toLocalDateTime() : null)
                .updatedAt(rs.getTimestamp("updated_at") != null ?
                        rs.getTimestamp("updated_at").toLocalDateTime() : null)
                .createdBy(rs.getString("created_by"))
                .updatedBy(rs.getString("updated_by"))
                .deletedAt(rs.getTimestamp("deleted_at") != null ?
                        rs.getTimestamp("deleted_at").toLocalDateTime() : null)
                .build();
    }
}