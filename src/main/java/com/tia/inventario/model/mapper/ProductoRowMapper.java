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
                .categoriaNombre(hasColumn(rs, "categoria_nombre") ? rs.getString("categoria_nombre") : null)
                .proveedorId(rs.getLong("proveedor_id"))
                .proveedorNombre(hasColumn(rs, "proveedor_nombre") ? rs.getString("proveedor_nombre") : null)
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

    private boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
        java.sql.ResultSetMetaData rsmd = rs.getMetaData();
        int columns = rsmd.getColumnCount();
        for (int x = 1; x <= columns; x++) {
            if (columnName.equals(rsmd.getColumnLabel(x).toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}