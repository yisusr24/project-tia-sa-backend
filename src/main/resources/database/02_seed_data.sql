-- ============================================================================
-- DATA INICIAL DE PRUEBA (SEED DATA)
-- ============================================================================

-- Usuarios
INSERT INTO usuarios (nombre_usuario, correo, clave, nombre, apellido, telefono, activo, created_by) VALUES
('superadmin', 'superadmin@inventario.com', '12345', 'Super', 'Administrador', '999888777', TRUE, 'SYSTEM'),
('jperez', 'jperez@inventario.com', '123456', 'Juan', 'Pérez', '987654321', TRUE, 'superadmin'),
('mgarcia', 'mgarcia@inventario.com', '1234567', 'María', 'García', '976543210', TRUE, 'superadmin'),
('lrodriguez', 'lrodriguez@inventario.com', '12345678', 'Luis', 'Rodríguez', '965432109', TRUE, 'superadmin');

-- Roles
INSERT INTO roles (nombre, descripcion) VALUES
('SUPERADMIN', 'Super administrador con acceso total al sistema'),
('GERENTE', 'Gerente con acceso a reportes'),
('VENDEDOR', 'Vendedor con acceso a ventas'),
('ALMACENERO', 'Encargado de inventario');

-- Usuarios_Roles
INSERT INTO usuarios_roles (usuario_id, rol_id, created_by) VALUES
(1, 1, 'superadmin'), (2, 2, 'superadmin'), (3, 3, 'superadmin'), (4, 4, 'superadmin');

-- Categorias
INSERT INTO categorias (nombre, descripcion, created_by) VALUES
('Electrónica', 'Productos electrónicos', 'superadmin'),
('Alimentos', 'Productos alimenticios', 'superadmin'),
('Bebidas', 'Bebidas y refrescos', 'superadmin'),
('Limpieza', 'Productos de limpieza', 'superadmin'),
('Snacks', 'Papas, galletas, dulces', 'superadmin');

INSERT INTO categorias (nombre, descripcion, categoria_padre_id, created_by) VALUES
('Laptops', 'Computadoras portátiles', 1, 'superadmin'),
('Celulares', 'Teléfonos móviles', 1, 'superadmin'),
('Lácteos', 'Productos lácteos', 2, 'superadmin');

-- Proveedores
INSERT INTO proveedores (razon_social, ruc, correo, telefono, direccion, ciudad, contacto_nombre, created_by) VALUES
('Distribuidora Tech SAC', '0992345678001', 'ventas@tech.com', '04-2567890', 'Av. 9 de Octubre 123', 'Guayaquil', 'Pedro Sánchez', 'superadmin'),
('Alimentos del Ecuador SA', '0991234567001', 'info@alimentos.com', '04-2678901', 'Av. Francisco de Orellana 456', 'Guayaquil', 'Rosa Vargas', 'superadmin'),
('Bebidas Premium EIRL', '0993456789001', 'contacto@bebidas.com', '04-2789012', 'Av. Carlos Julio Arosemena 789', 'Guayaquil', 'Mario Torres', 'superadmin');

-- Unidades de Medida
INSERT INTO unidades_medida (codigo, nombre, simbolo) VALUES
('UND', 'Unidad', 'Und'),
('KG', 'Kilogramo', 'Kg'),
('LT', 'Litro', 'Lt'),
('CJ', 'Caja', 'Caja'),
('PAQ', 'Paquete', 'Paq');

-- Productos
INSERT INTO productos (codigo, nombre, descripcion, categoria_id, proveedor_id, unidad_medida_id, precio_compra, precio_venta, stock_minimo, stock_maximo, created_by, activo) VALUES
('PROD001', 'Laptop HP Pavilion 15', 'Intel i5, 8GB RAM, 512GB SSD', 5, 1, 1, 2500.00, 3200.00, 5, 50, 'superadmin', true),
('PROD002', 'iPhone 14 128GB', 'Smartphone Apple', 6, 1, 1, 3500.00, 4200.00, 3, 30, 'superadmin', true),
('PROD003', 'Leche Gloria 1L', 'Leche evaporada', 7, 2, 1, 3.50, 4.80, 50, 200, 'superadmin', true),
('PROD004', 'Coca Cola 1.5L', 'Gaseosa', 3, 3, 1, 3.50, 5.50, 60, 250, 'superadmin', true),
('PROD005', 'Detergente Ariel 1kg', 'Detergente en polvo', 4, 2, 2, 8.00, 12.50, 30, 100, 'superadmin', true),
('PROD006', 'Arroz Superior 1kg', 'Arroz blanco', 2, 2, 2, 3.00, 4.50, 80, 500, 'superadmin', true),
('PROD007', 'Atún Real Lomo', 'Conserva de atún', 2, 2, 1, 1.10, 1.60, 40, 200, 'superadmin', true),
('PROD008', 'Fideos Tallarín 400g', 'Pasta larga', 2, 2, 1, 0.60, 0.95, 50, 300, 'superadmin', true),
('PROD009', 'Jabón Protex Avena', 'Jabón de tocador', 4, 2, 1, 0.70, 1.10, 100, 500, 'superadmin', true),
('PROD010', 'Yogurt Toni Frutilla', 'Yogurt bebible', 7, 2, 1, 1.80, 2.50, 30, 100, 'superadmin', true),
('PROD011', 'Queso Mozzarella Kiosko', 'Queso rallado', 7, 2, 2, 5.50, 7.80, 10, 50, 'superadmin', true),
('PROD012', 'Galletas Oreo Paquete', 'Galletas rellenas', 5, 2, 5, 0.40, 0.75, 100, 400, 'superadmin', true),
('PROD013', 'Papas Ruffles Original', 'Papas fritas', 5, 2, 5, 1.20, 1.80, 40, 150, 'superadmin', true),
('PROD014', 'Chocolate Manicho', 'Barra de chocolate', 5, 2, 1, 0.35, 0.60, 100, 600, 'superadmin', true),
('PROD015', 'Cerveza Club Premium', 'Botella 330ml', 3, 3, 1, 1.25, 1.75, 100, 500, 'superadmin', true),
('PROD016', 'Azúcar San Carlos 2Kg', 'Azúcar blanca', 2, 2, 2, 1.80, 2.40, 50, 300, 'superadmin', true),
('PROD017', 'Desinfectante Fabuloso', 'Limpiador de pisos', 4, 2, 3, 1.50, 2.25, 30, 120, 'superadmin', true),
('PROD018', 'Helado Pingüino Litro', 'Helado de vainilla', 7, 2, 1, 3.50, 5.50, 10, 50, 'superadmin', true),
('PROD019', 'Aceite La Favorita 1L', 'Aceite vegetal', 2, 2, 3, 2.50, 3.25, 20, 100, 'superadmin', true),
('PROD020', 'Pepsi 1.5L', 'Gaseosa negra', 3, 3, 1, 1.40, 1.90, 20, 100, 'superadmin', true),
('PROD021', 'Gatorade Blue 750ml', 'Bebida hidratante', 3, 3, 1, 0.80, 1.25, 24, 100, 'superadmin', true),
('PROD022', 'Té Lipton Limón 500ml', 'Té helado', 3, 3, 1, 0.60, 1.00, 24, 100, 'superadmin', true),
('PROD023', 'Red Bull 250ml', 'Bebida energizante', 3, 3, 1, 1.50, 2.50, 24, 100, 'superadmin', true),
('PROD024', 'Doritos Queso 180g', 'Tortillas de maíz', 5, 2, 5, 1.10, 1.60, 15, 60, 'superadmin', true),
('PROD025', 'Cheetos Puffs 150g', 'Snack de queso', 5, 2, 5, 1.00, 1.50, 15, 60, 'superadmin', true),
('PROD026', 'Maní Salado 50g', 'Maní tostado', 5, 2, 5, 0.30, 0.50, 50, 200, 'superadmin', true),
('PROD027', 'Mantequilla Bonella 250g', 'Margarina con sal', 8, 2, 1, 1.20, 1.80, 20, 80, 'superadmin', true),
('PROD028', 'Queso Crema Toni 200g', 'Queso para untar', 8, 2, 1, 1.80, 2.60, 10, 40, 'superadmin', true),
('PROD029', 'Leche Chocolatada 200ml', 'Leche sabor chocolate', 8, 2, 1, 0.50, 0.85, 48, 150, 'superadmin', true),
('PROD030', 'Cloro Clorox 1L', 'Desinfectante blanqueador', 4, 2, 3, 1.00, 1.50, 30, 100, 'superadmin', true),
('PROD031', 'Limpiavidrios Mr. Musculo', 'Limpiador de ventanas', 4, 2, 1, 2.20, 3.10, 10, 50, 'superadmin', true),
('PROD032', 'Esponja Scotch Brite', 'Esponja de cocina', 4, 2, 1, 0.40, 0.80, 50, 200, 'superadmin', true),
('PROD033', 'Lentejas 500g', 'Lenteja seca', 2, 2, 5, 0.90, 1.40, 30, 100, 'superadmin', true),
('PROD034', 'Fréjol Negro 500g', 'Fréjol seco', 2, 2, 5, 1.00, 1.50, 30, 100, 'superadmin', true),
('PROD035', 'Sal Yodada Crisal 1kg', 'Sal de cocina', 2, 2, 2, 0.40, 0.70, 50, 200, 'superadmin', true),
('PROD036', 'Pilas Duracell AA Par', 'Pilas alcalinas', 1, 1, 5, 1.50, 2.50, 20, 100, 'superadmin', true),
('PROD037', 'Cable USB-C 1m', 'Cable de carga rápida', 1, 1, 1, 3.00, 5.00, 10, 50, 'superadmin', true),
('PROD038', 'Audífonos Básicos Sony', 'Auriculares con cable', 1, 1, 1, 8.00, 12.00, 5, 20, 'superadmin', true),
('PROD039', 'Papel Higiénico Familia 4u', 'Papel doble hoja', 4, 2, 5, 1.80, 2.50, 40, 150, 'superadmin', true),
('PROD040', 'Servilletas Favorita 100u', 'Servilletas de papel', 4, 2, 5, 0.70, 1.10, 30, 120, 'superadmin', true),
('PROD041', 'Galleta Maria 200g', 'Galletas de vainilla', 5, 2, 5, 0.80, 1.20, 20, 100, 'superadmin', true),
('PROD042', 'Galleta Wafer Vainilla', 'Galletas tipo wafer', 5, 2, 5, 0.60, 0.90, 20, 100, 'superadmin', true),
('PROD043', 'Galleta Wafer Fresa', 'Galletas tipo wafer', 5, 2, 5, 0.60, 0.90, 20, 100, 'superadmin', true),
('PROD044', 'Galleta Wafer Chocolate', 'Galletas tipo wafer', 5, 2, 5, 0.60, 0.90, 20, 100, 'superadmin', true),
('PROD045', 'Galleta Club Social Original', 'Galletas saladas', 5, 2, 5, 0.50, 0.80, 30, 150, 'superadmin', true),
('PROD046', 'Galleta Club Social Integral', 'Galletas saladas integrales', 5, 2, 5, 0.60, 0.90, 20, 100, 'superadmin', true),
('PROD047', 'Galleta Ritz Queso', 'Galletas con queso', 5, 2, 5, 0.70, 1.10, 20, 100, 'superadmin', true),
('PROD048', 'Arroz Flor 2kg', 'Arroz grano largo', 2, 2, 2, 2.50, 3.20, 15, 80, 'superadmin', true),
('PROD049', 'Arroz Viejo 5kg', 'Arroz añejo seleccionado', 2, 2, 2, 6.00, 8.50, 10, 50, 'superadmin', true),
('PROD050', 'Arroz Integral 1kg', 'Arroz dietético', 2, 2, 2, 1.50, 2.10, 10, 50, 'superadmin', true),
('PROD051', 'Leche Descremada Vita 1L', 'Leche 0% grasa', 7, 2, 3, 1.00, 1.30, 30, 120, 'superadmin', true),
('PROD052', 'Leche Semidescremada Vita', 'Leche baja en grasa', 7, 2, 3, 0.95, 1.25, 30, 120, 'superadmin', true),
('PROD053', 'Leche Saborizada Fresa 200ml', 'Leche para niños', 7, 2, 1, 0.60, 0.90, 40, 200, 'superadmin', true),
('PROD054', 'Leche Saborizada Vainilla 200ml', 'Leche para niños', 7, 2, 1, 0.60, 0.90, 40, 200, 'superadmin', true),
('PROD055', 'Aceite Girasol 1L', 'Aceite puro de girasol', 2, 2, 3, 3.50, 4.80, 15, 60, 'superadmin', true),
('PROD056', 'Aceite Oliva Extra Virgen 500ml', 'Aceite gourmet', 2, 2, 3, 6.00, 9.50, 5, 20, 'superadmin', true),
('PROD057', 'Aceite Canola 1L', 'Aceite saludable', 2, 2, 3, 3.00, 4.20, 15, 60, 'superadmin', true),
('PROD058', 'Coca Cola Zero 1.5L', 'Gaseosa sin azúcar', 3, 3, 1, 1.50, 2.00, 20, 100, 'superadmin', true),
('PROD059', 'Coca Cola Light 1.5L', 'Gaseosa baja calorías', 3, 3, 1, 1.50, 2.00, 20, 100, 'superadmin', true),
('PROD060', 'Coca Cola 3L', 'Gaseosa familiar', 3, 3, 1, 2.80, 3.50, 15, 80, 'superadmin', true);

-- Locales
INSERT INTO locales (codigo, nombre, direccion, ciudad, canton, pais, telefono, tipo, created_by) VALUES
('LOC001', 'Tienda Principal Centro', 'Av. 9 de Octubre y Malecón', 'Guayaquil', 'Guayaquil', 'Ecuador', '04-2345678', 'TIENDA', 'superadmin'),
('LOC002', 'Sucursal Samborondón', 'Av. Samborondón, Plaza Lagos', 'Samborondón', 'Guayaquil', 'Ecuador', '04-2456789', 'TIENDA', 'superadmin'),
('LOC003', 'Almacén Daule', 'Vía Daule Km 8.5', 'Daule', 'Guayaquil', 'Ecuador', '04-2567890', 'ALMACEN', 'superadmin'),
('LOC004', 'Supermercado Norte', 'Av. Juan Tanca Marengo', 'Guayaquil', 'Guayaquil', 'Ecuador', '04-2111111', 'SUPERMERCADO', 'superadmin'),
('LOC005', 'Mini TIA La Garzota', 'Av. Guillermo Pareja', 'Guayaquil', 'Guayaquil', 'Ecuador', '04-2222222', 'TIENDA', 'superadmin'),
('LOC006', 'TIA Centro Sur', 'Av. 25 de Julio', 'Guayaquil', 'Guayaquil', 'Ecuador', '04-2333333', 'TIENDA', 'superadmin'),
('LOC007', 'Bodega Durán', 'Vía Durán Boliche', 'Durán', 'Durán', 'Ecuador', '04-2444444', 'BODEGA', 'superadmin'),
('LOC008', 'Supermercado Los Ceibos', 'Av. del Bombero', 'Guayaquil', 'Guayaquil', 'Ecuador', '04-2555555', 'SUPERMERCADO', 'superadmin'),
('LOC009', 'TIA Pascuales', 'Vía a Daule Km 12', 'Guayaquil', 'Guayaquil', 'Ecuador', '04-2666666', 'TIENDA', 'superadmin'),
('LOC010', 'Mini TIA Urdesa', 'Av. Víctor Emilio Estrada', 'Guayaquil', 'Guayaquil', 'Ecuador', '04-2777777', 'TIENDA', 'superadmin'),
('LOC011', 'TIA Machala Centro', 'Av. 25 de Junio', 'Machala', 'Machala', 'Ecuador', '07-2888888', 'TIENDA', 'superadmin'),
('LOC012', 'Bodega Machala', 'Vía Puerto Bolívar', 'Machala', 'Machala', 'Ecuador', '07-2999999', 'BODEGA', 'superadmin'),
('LOC013', 'TIA Manta Tarqui', 'Av. 113', 'Manta', 'Manta', 'Ecuador', '05-2000000', 'TIENDA', 'superadmin'),
('LOC014', 'Supermercado Portoviejo', 'Av. Manabí', 'Portoviejo', 'Portoviejo', 'Ecuador', '05-2111111', 'SUPERMERCADO', 'superadmin'),
('LOC015', 'TIA Cuenca Historic', 'Calle Larga', 'Cuenca', 'Cuenca', 'Ecuador', '07-2222222', 'TIENDA', 'superadmin'),
('LOC016', 'Bodega Cuenca', 'Parque Industrial', 'Cuenca', 'Cuenca', 'Ecuador', '07-2333333', 'BODEGA', 'superadmin'),
('LOC017', 'TIA Quito Centro', 'Calle Guayaquil', 'Quito', 'Quito', 'Ecuador', '02-2444444', 'TIENDA', 'superadmin'),
('LOC018', 'Supermercado Iñaquito', 'Av. Amazonas', 'Quito', 'Quito', 'Ecuador', '02-2555555', 'SUPERMERCADO', 'superadmin'),
('LOC019', 'TIA Cumbayá', 'Av. Interoceánica', 'Quito', 'Quito', 'Ecuador', '02-2666666', 'TIENDA', 'superadmin'),
('LOC020', 'Bodega Quito Norte', 'Carcelén Industrial', 'Quito', 'Quito', 'Ecuador', '02-2777777', 'BODEGA', 'superadmin');

-- Inventario Inicial
INSERT INTO inventario (producto_id, local_id, stock_actual, stock_minimo, created_by) VALUES
(1, 1, 8, 5, 'superadmin'), (2, 1, 5, 3, 'superadmin'), (3, 1, 120, 50, 'superadmin'), (4, 1, 180, 60, 'superadmin'), (5, 1, 60, 30, 'superadmin'), (6, 1, 150, 80, 'superadmin'),
(1, 2, 3, 2, 'superadmin'), (2, 2, 2, 2, 'superadmin'), (3, 2, 80, 40, 'superadmin'), (4, 2, 140, 50, 'superadmin'), (5, 2, 45, 25, 'superadmin'), (6, 2, 100, 60, 'superadmin'),
(1, 3, 15, 10, 'superadmin'), (2, 3, 10, 5, 'superadmin'), (3, 3, 300, 100, 'superadmin'), (4, 3, 350, 120, 'superadmin'), (5, 3, 150, 60, 'superadmin'), (6, 3, 400, 150, 'superadmin');

INSERT INTO movimientos_inventario (producto_id, local_id, tipo_movimiento, cantidad, stock_anterior, stock_nuevo, precio_unitario, motivo, created_by) VALUES
(1, 1, 'ENTRADA', 8, 0, 8, 2500.00, 'Stock inicial', 'superadmin'),
(3, 1, 'ENTRADA', 120, 0, 120, 3.50, 'Stock inicial', 'superadmin'),
(4, 1, 'ENTRADA', 180, 0, 180, 3.50, 'Stock inicial', 'superadmin');

INSERT INTO ventas (local_id, vendedor_id, cliente_nombre, subtotal, impuesto, total, metodo_pago, created_by)
VALUES (1, 3, 'Pedro Castillo', 42.37, 7.63, 50.00, 'EFECTIVO', 'mgarcia');

INSERT INTO detalle_ventas (venta_id, producto_id, cantidad, precio_unitario, subtotal, total) VALUES 
(1, 3, 5, 4.80, 24.00, 24.00),
(1, 4, 3, 5.50, 16.50, 16.50);

INSERT INTO ventas (local_id, vendedor_id, subtotal, impuesto, total, metodo_pago, created_by, created_at)
VALUES (1, 3, 127.12, 22.88, 150.00, 'EFECTIVO', 'mgarcia', CURRENT_TIMESTAMP - INTERVAL '1 day');

INSERT INTO detalle_ventas (venta_id, producto_id, cantidad, precio_unitario, subtotal, total) VALUES 
(2, 6, 10, 4.50, 45.00, 45.00),
(2, 5, 4, 12.50, 50.00, 50.00);

SELECT 'Datos cargados correctamente:' AS info;
SELECT 'Usuarios: ' || COUNT(*) FROM usuarios;
SELECT 'Productos: ' || COUNT(*) FROM productos;
SELECT 'Locales: ' || COUNT(*) FROM locales;
SELECT 'Inventario: ' || COUNT(*) FROM inventario;
SELECT 'Ventas: ' || COUNT(*) FROM ventas;