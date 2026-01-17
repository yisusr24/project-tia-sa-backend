package com.tia.inventario.constant;
public class SqlQueries {
    public static final String PRODUCTO_FIND_ALL = """
            SELECT p.*, c.nombre as categoria_nombre, pr.razon_social as proveedor_nombre 
            FROM productos p
            LEFT JOIN categorias c ON p.categoria_id = c.id
            LEFT JOIN proveedores pr ON p.proveedor_id = pr.id
            WHERE p.deleted_at IS NULL
            ORDER BY p.id DESC
            """;
    public static final String PRODUCTO_FIND_ALL_PAGINATED = """
            SELECT p.*, c.nombre as categoria_nombre, pr.razon_social as proveedor_nombre 
            FROM productos p
            LEFT JOIN categorias c ON p.categoria_id = c.id
            LEFT JOIN proveedores pr ON p.proveedor_id = pr.id
            WHERE p.deleted_at IS NULL
            ORDER BY p.id DESC
            LIMIT ? OFFSET ?
            """;
    public static final String PRODUCTO_COUNT = """
            SELECT COUNT(*) FROM productos
            WHERE deleted_at IS NULL
            """;
    public static final String PRODUCTO_FIND_BY_ID = """
            SELECT p.*, c.nombre as categoria_nombre, pr.razon_social as proveedor_nombre 
            FROM productos p
            LEFT JOIN categorias c ON p.categoria_id = c.id
            LEFT JOIN proveedores pr ON p.proveedor_id = pr.id
            WHERE p.id = ? AND p.deleted_at IS NULL
            """;
    public static final String PRODUCTO_FIND_BY_CODIGO = """
            SELECT p.*, c.nombre as categoria_nombre, pr.razon_social as proveedor_nombre 
            FROM productos p
            LEFT JOIN categorias c ON p.categoria_id = c.id
            LEFT JOIN proveedores pr ON p.proveedor_id = pr.id
            WHERE p.codigo = ? AND p.deleted_at IS NULL
            """;
    public static final String PRODUCTO_SEARCH = """
            SELECT p.*, c.nombre as categoria_nombre, pr.razon_social as proveedor_nombre 
            FROM productos p
            LEFT JOIN categorias c ON p.categoria_id = c.id
            LEFT JOIN proveedores pr ON p.proveedor_id = pr.id
            WHERE (LOWER(p.nombre) LIKE LOWER(?) OR LOWER(p.codigo) LIKE LOWER(?))
              AND p.deleted_at IS NULL
            ORDER BY p.nombre
            """;
    public static final String PRODUCTO_INSERT = """
            INSERT INTO productos (
                codigo, nombre, descripcion, categoria_id, proveedor_id, unidad_medida_id,
                precio_compra, precio_venta, precio_venta_minimo, stock_minimo, stock_maximo,
                imagen_url, es_perecedero, dias_vigencia, created_by
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
    public static final String PRODUCTO_UPDATE = """
            UPDATE productos SET
                nombre = ?, descripcion = ?, categoria_id = ?, proveedor_id = ?,
                precio_compra = ?, precio_venta = ?, precio_venta_minimo = ?,
                stock_minimo = ?, stock_maximo = ?, imagen_url = ?,
                es_perecedero = ?, dias_vigencia = ?, updated_by = ?
            WHERE id = ? AND deleted_at IS NULL
            """;
    public static final String PRODUCTO_DELETE = """
            UPDATE productos
            SET deleted_at = CURRENT_TIMESTAMP
            WHERE id = ?
            """;
    public static final String PRODUCTO_FIND_DELETED = """
            SELECT * FROM productos
            WHERE deleted_at IS NOT NULL
            ORDER BY id DESC
            """;
    public static final String PRODUCTO_FIND_DELETED_PAGINATED = """
            SELECT * FROM productos
            WHERE deleted_at IS NOT NULL
            ORDER BY id DESC
            LIMIT ? OFFSET ?
            """;
    public static final String PRODUCTO_COUNT_DELETED = """
            SELECT COUNT(*) FROM productos
            WHERE deleted_at IS NOT NULL
            """;
    public static final String PRODUCTO_RESTORE = """
            UPDATE productos
            SET deleted_at = NULL, activo = TRUE
            WHERE id = ?
            """;
    public static final String LOCAL_FIND_ALL = """
            SELECT * FROM locales
            WHERE deleted_at IS NULL
            ORDER BY id
            """;
    public static final String LOCAL_FIND_BY_ID = """
            SELECT * FROM locales
            WHERE id = ? AND deleted_at IS NULL
            """;
    public static final String LOCAL_INSERT = """
            INSERT INTO locales (
                codigo, nombre, direccion, ciudad, canton, pais,
                telefono, correo, tipo, activo, created_by
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
    public static final String LOCAL_UPDATE = """
            UPDATE locales SET
                nombre = ?, direccion = ?, ciudad = ?, canton = ?, pais = ?,
                telefono = ?, correo = ?, tipo = ?, activo = ?, updated_by = ?
            WHERE id = ? AND deleted_at IS NULL
            """;
    public static final String LOCAL_DELETE = """
            UPDATE locales
            SET deleted_at = CURRENT_TIMESTAMP
            WHERE id = ?
            """;
    public static final String LOCAL_FIND_DELETED = """
            SELECT * FROM locales
            WHERE deleted_at IS NOT NULL
            ORDER BY id
            """;
    public static final String LOCAL_RESTORE = """
            UPDATE locales
            SET deleted_at = NULL, activo = TRUE
            WHERE id = ?
            """;
    public static final String INVENTARIO_FIND_BY_LOCAL = """
            SELECT i.*, p.codigo, p.nombre AS producto_nombre
            FROM inventario i
            JOIN productos p ON i.producto_id = p.id
            WHERE i.local_id = ?
            ORDER BY p.nombre
            """;
    public static final String INVENTARIO_STOCK_BAJO = """
            SELECT * FROM vista_productos_stock_bajo
            """;
    public static final String INVENTARIO_UPDATE_STOCK = """
            UPDATE inventario
            SET stock_actual = ?, updated_by = ?
            WHERE producto_id = ? AND local_id = ?
            """;
    public static final String VENTA_INSERT = """
            INSERT INTO ventas (
                local_id, vendedor_id, cliente_nombre, cliente_documento,
                subtotal, impuesto, descuento, total, metodo_pago,
                observaciones, created_by
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
    public static final String VENTA_DETALLE_INSERT = """
            INSERT INTO detalle_ventas (
                venta_id, producto_id, cantidad, precio_unitario,
                subtotal, descuento, total
            ) VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
    public static final String VENTA_FIND_BY_ID = """
            SELECT * FROM ventas WHERE id = ?
            """;
    public static final String REPORTE_STOCK_CONSOLIDADO = """
            SELECT * FROM vista_stock_consolidado
            """;
    public static final String REPORTE_VENTAS_DIARIAS = """
            SELECT * FROM vista_ventas_diarias
            WHERE fecha >= ?
            ORDER BY fecha DESC
            """;
    public static final String REPORTE_MAS_VENDIDOS = """
            SELECT * FROM vista_productos_mas_vendidos
            LIMIT ?
            """;
    private SqlQueries() {
    }
}