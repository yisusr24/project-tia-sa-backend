-- ============================================================================
-- Base de Datos: PostgreSQL 14
-- Autor: Jesus Rosales Reyes
-- ============================================================================

SET client_encoding = 'UTF8';
SET timezone = 'America/Lima';

-- ============================================================================
-- AUTENTICACION
-- ============================================================================

CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    nombre_usuario VARCHAR(50) NOT NULL UNIQUE,
    correo VARCHAR(100) NOT NULL UNIQUE,
    clave VARCHAR(255) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    activo BOOLEAN DEFAULT TRUE,
    fecha_ultimo_acceso TIMESTAMP,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50),
    updated_by VARCHAR(50),
    deleted_at TIMESTAMP
);

CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion TEXT,
    activo BOOLEAN DEFAULT TRUE,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50),
    updated_by VARCHAR(50)
);

CREATE TABLE usuarios_roles (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    rol_id BIGINT NOT NULL,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50),
    
    UNIQUE(usuario_id, rol_id)
);

-- ============================================================================
-- CATALOGOS
-- ============================================================================

CREATE TABLE categorias (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    categoria_padre_id BIGINT,
    activo BOOLEAN DEFAULT TRUE,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50),
    updated_by VARCHAR(50),
    deleted_at TIMESTAMP
);

CREATE TABLE proveedores (
    id BIGSERIAL PRIMARY KEY,
    razon_social VARCHAR(200) NOT NULL,
    ruc VARCHAR(20) NOT NULL UNIQUE,
    correo VARCHAR(100),
    telefono VARCHAR(20),
    direccion TEXT,
    ciudad VARCHAR(100),
    pais VARCHAR(100),
    contacto_nombre VARCHAR(100),
    contacto_telefono VARCHAR(20),
    activo BOOLEAN DEFAULT TRUE,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50),
    updated_by VARCHAR(50),
    deleted_at TIMESTAMP
);

CREATE TABLE unidades_medida (
    id BIGSERIAL PRIMARY KEY,
    codigo VARCHAR(10) NOT NULL UNIQUE,
    nombre VARCHAR(50) NOT NULL,
    simbolo VARCHAR(10),
    activo BOOLEAN DEFAULT TRUE,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================================
-- PRODUCTOS
-- ============================================================================

CREATE TABLE productos (
    id BIGSERIAL PRIMARY KEY,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    nombre VARCHAR(200) NOT NULL,
    descripcion TEXT,
    categoria_id BIGINT,
    proveedor_id BIGINT,
    unidad_medida_id BIGINT NOT NULL,
    precio_compra DECIMAL(12, 2),
    precio_venta DECIMAL(12, 2) NOT NULL,
    precio_venta_minimo DECIMAL(12, 2),
    stock_minimo INTEGER DEFAULT 10,
    stock_maximo INTEGER,
    imagen_url VARCHAR(500),
    es_perecedero BOOLEAN DEFAULT FALSE,
    dias_vigencia INTEGER,
    activo BOOLEAN DEFAULT TRUE,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50),
    updated_by VARCHAR(50),
    deleted_at TIMESTAMP,
    
    CONSTRAINT check_precio_venta_positivo CHECK (precio_venta > 0),
    CONSTRAINT check_precio_compra_positivo CHECK (precio_compra IS NULL OR precio_compra >= 0)
);

-- ============================================================================
-- LOCALES
-- ============================================================================

CREATE TABLE locales (
    id BIGSERIAL PRIMARY KEY,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL,
    direccion TEXT NOT NULL,
    ciudad VARCHAR(100),
    canton VARCHAR(100),
    pais VARCHAR(100),
    telefono VARCHAR(20),
    correo VARCHAR(100),
    tipo VARCHAR(50),
    es_principal BOOLEAN DEFAULT FALSE,
    activo BOOLEAN DEFAULT TRUE,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50),
    updated_by VARCHAR(50),
    deleted_at TIMESTAMP
);

-- ============================================================================
-- INVENTARIO
-- ============================================================================

CREATE TABLE inventario (
    id BIGSERIAL PRIMARY KEY,
    producto_id BIGINT NOT NULL,
    local_id BIGINT NOT NULL,
    stock_actual INTEGER NOT NULL DEFAULT 0,
    stock_minimo INTEGER DEFAULT 10,
    stock_maximo INTEGER,
    ubicacion VARCHAR(50),
    lote VARCHAR(50),
    fecha_vencimiento DATE,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50),
    updated_by VARCHAR(50),
    
    CONSTRAINT check_stock_no_negativo CHECK (stock_actual >= 0)
);

CREATE TABLE movimientos_inventario (
    id BIGSERIAL PRIMARY KEY,
    producto_id BIGINT NOT NULL,
    local_id BIGINT NOT NULL,
    tipo_movimiento VARCHAR(20) NOT NULL,
    cantidad INTEGER NOT NULL,
    stock_anterior INTEGER NOT NULL,
    stock_nuevo INTEGER NOT NULL,
    precio_unitario DECIMAL(12, 2),
    costo_total DECIMAL(12, 2),
    local_destino_id BIGINT,
    venta_id BIGINT,
    motivo TEXT,
    numero_documento VARCHAR(50),
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50),
    
    CONSTRAINT check_cantidad_positiva CHECK (cantidad > 0)
);

-- ============================================================================
-- VENTAS
-- ============================================================================

CREATE TABLE ventas (
    id BIGSERIAL PRIMARY KEY,
    numero_venta VARCHAR(50) NOT NULL UNIQUE,
    local_id BIGINT NOT NULL,
    vendedor_id BIGINT NOT NULL,
    cliente_nombre VARCHAR(200),
    cliente_documento VARCHAR(20),
    subtotal DECIMAL(12, 2) NOT NULL,
    impuesto DECIMAL(12, 2) DEFAULT 0,
    descuento DECIMAL(12, 2) DEFAULT 0,
    total DECIMAL(12, 2) NOT NULL,
    metodo_pago VARCHAR(20),
    estado VARCHAR(20) DEFAULT 'COMPLETADA',
    observaciones TEXT,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(50),
    updated_by VARCHAR(50),
    
    CONSTRAINT check_total_positivo CHECK (total >= 0)
);

CREATE TABLE detalle_ventas (
    id BIGSERIAL PRIMARY KEY,
    venta_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad INTEGER NOT NULL,
    precio_unitario DECIMAL(12, 2) NOT NULL,
    subtotal DECIMAL(12, 2) NOT NULL,
    descuento DECIMAL(12, 2) DEFAULT 0,
    total DECIMAL(12, 2) NOT NULL,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT check_cantidad_positiva CHECK (cantidad > 0),
    CONSTRAINT check_precio_positivo CHECK (precio_unitario > 0)
);

-- ============================================================================
-- FOREIGN KEYS
-- ============================================================================

ALTER TABLE usuarios_roles ADD CONSTRAINT fk_usuarios_roles_usuario
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE;
ALTER TABLE usuarios_roles ADD CONSTRAINT fk_usuarios_roles_rol 
    FOREIGN KEY (rol_id) REFERENCES roles(id) ON DELETE CASCADE;

ALTER TABLE categorias ADD CONSTRAINT fk_categorias_padre
    FOREIGN KEY (categoria_padre_id) REFERENCES categorias(id);

ALTER TABLE productos ADD CONSTRAINT fk_productos_categoria 
    FOREIGN KEY (categoria_id) REFERENCES categorias(id);
ALTER TABLE productos ADD CONSTRAINT fk_productos_proveedor 
    FOREIGN KEY (proveedor_id) REFERENCES proveedores(id);
ALTER TABLE productos ADD CONSTRAINT fk_productos_unidad_medida 
    FOREIGN KEY (unidad_medida_id) REFERENCES unidades_medida(id);

ALTER TABLE inventario ADD CONSTRAINT fk_inventario_producto
    FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE RESTRICT;
ALTER TABLE inventario ADD CONSTRAINT fk_inventario_local 
    FOREIGN KEY (local_id) REFERENCES locales(id) ON DELETE RESTRICT;

ALTER TABLE inventario ADD CONSTRAINT uk_inventario_producto_local_lote 
    UNIQUE (producto_id, local_id, lote);

ALTER TABLE movimientos_inventario ADD CONSTRAINT fk_movimientos_producto
    FOREIGN KEY (producto_id) REFERENCES productos(id);
ALTER TABLE movimientos_inventario ADD CONSTRAINT fk_movimientos_local 
    FOREIGN KEY (local_id) REFERENCES locales(id);
ALTER TABLE movimientos_inventario ADD CONSTRAINT fk_movimientos_local_destino 
    FOREIGN KEY (local_destino_id) REFERENCES locales(id);
ALTER TABLE movimientos_inventario ADD CONSTRAINT fk_movimientos_venta 
    FOREIGN KEY (venta_id) REFERENCES ventas(id);

ALTER TABLE ventas ADD CONSTRAINT fk_ventas_local
    FOREIGN KEY (local_id) REFERENCES locales(id);
ALTER TABLE ventas ADD CONSTRAINT fk_ventas_vendedor 
    FOREIGN KEY (vendedor_id) REFERENCES usuarios(id);

ALTER TABLE detalle_ventas ADD CONSTRAINT fk_detalle_venta
    FOREIGN KEY (venta_id) REFERENCES ventas(id) ON DELETE CASCADE;
ALTER TABLE detalle_ventas ADD CONSTRAINT fk_detalle_producto 
    FOREIGN KEY (producto_id) REFERENCES productos(id);

-- ============================================================================
-- INDICES
-- ============================================================================

CREATE INDEX idx_usuarios_nombre_usuario ON usuarios(nombre_usuario);

CREATE INDEX idx_usuarios_roles_usuario ON usuarios_roles(usuario_id);
CREATE INDEX idx_usuarios_roles_rol ON usuarios_roles(rol_id);

CREATE INDEX idx_categorias_padre ON categorias(categoria_padre_id);

CREATE INDEX idx_proveedores_ruc ON proveedores(ruc);

CREATE INDEX idx_productos_codigo ON productos(codigo);
CREATE INDEX idx_productos_nombre ON productos USING gin(to_tsvector('spanish', nombre));
CREATE INDEX idx_productos_categoria ON productos(categoria_id);
CREATE INDEX idx_productos_proveedor ON productos(proveedor_id);

CREATE INDEX idx_locales_codigo ON locales(codigo);

CREATE INDEX idx_inventario_producto ON inventario(producto_id);
CREATE INDEX idx_inventario_local ON inventario(local_id);
CREATE INDEX idx_inventario_stock_bajo ON inventario(stock_actual) 
    WHERE stock_actual <= stock_minimo;
CREATE INDEX idx_inventario_vencimiento ON inventario(fecha_vencimiento) 
    WHERE fecha_vencimiento IS NOT NULL;

CREATE INDEX idx_movimientos_producto ON movimientos_inventario(producto_id);
CREATE INDEX idx_movimientos_local ON movimientos_inventario(local_id);
CREATE INDEX idx_movimientos_tipo ON movimientos_inventario(tipo_movimiento);
CREATE INDEX idx_movimientos_fecha ON movimientos_inventario(created_at);
CREATE INDEX idx_movimientos_venta ON movimientos_inventario(venta_id);

CREATE INDEX idx_ventas_numero ON ventas(numero_venta);
CREATE INDEX idx_ventas_local ON ventas(local_id);
CREATE INDEX idx_ventas_fecha ON ventas(created_at);
CREATE INDEX idx_ventas_estado ON ventas(estado);

CREATE INDEX idx_detalle_venta ON detalle_ventas(venta_id);
CREATE INDEX idx_detalle_producto ON detalle_ventas(producto_id);

-- ============================================================================
-- FUNCIONES Y TRIGGERS
-- ============================================================================

-- Funcion: Actualizar updated_at automaticamente
CREATE OR REPLACE FUNCTION actualizar_fecha_actualizacion_trigger()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Aplicar trigger a todas las tablas con updated_at
CREATE TRIGGER trigger_actualizar_usuarios
    BEFORE UPDATE ON usuarios
    FOR EACH ROW EXECUTE FUNCTION actualizar_fecha_actualizacion_trigger();

CREATE TRIGGER trigger_actualizar_roles
    BEFORE UPDATE ON roles
    FOR EACH ROW EXECUTE FUNCTION actualizar_fecha_actualizacion_trigger();

CREATE TRIGGER trigger_actualizar_categorias
    BEFORE UPDATE ON categorias
    FOR EACH ROW EXECUTE FUNCTION actualizar_fecha_actualizacion_trigger();

CREATE TRIGGER trigger_actualizar_proveedores
    BEFORE UPDATE ON proveedores
    FOR EACH ROW EXECUTE FUNCTION actualizar_fecha_actualizacion_trigger();

CREATE TRIGGER trigger_actualizar_unidades
    BEFORE UPDATE ON unidades_medida
    FOR EACH ROW EXECUTE FUNCTION actualizar_fecha_actualizacion_trigger();

CREATE TRIGGER trigger_actualizar_productos
    BEFORE UPDATE ON productos
    FOR EACH ROW EXECUTE FUNCTION actualizar_fecha_actualizacion_trigger();

CREATE TRIGGER trigger_actualizar_locales
    BEFORE UPDATE ON locales
    FOR EACH ROW EXECUTE FUNCTION actualizar_fecha_actualizacion_trigger();

CREATE TRIGGER trigger_actualizar_inventario
    BEFORE UPDATE ON inventario
    FOR EACH ROW EXECUTE FUNCTION actualizar_fecha_actualizacion_trigger();

CREATE TRIGGER trigger_actualizar_ventas
    BEFORE UPDATE ON ventas
    FOR EACH ROW EXECUTE FUNCTION actualizar_fecha_actualizacion_trigger();

-- Funcion: Actualizar stock en inventario despues de venta
CREATE OR REPLACE FUNCTION actualizar_stock_en_venta()
RETURNS TRIGGER AS $$
DECLARE
    v_stock_anterior INTEGER;
    v_stock_nuevo INTEGER;
    v_local_id BIGINT;
BEGIN
    -- Obtener local de la venta
    SELECT local_id INTO v_local_id FROM ventas WHERE id = NEW.venta_id;
    
    -- Obtener stock actual
    SELECT stock_actual INTO v_stock_anterior 
    FROM inventario 
    WHERE producto_id = NEW.producto_id AND local_id = v_local_id;
    
    -- Verificar que hay suficiente stock
    IF v_stock_anterior < NEW.cantidad THEN
        RAISE EXCEPTION 'Stock insuficiente. Disponible: %, Solicitado: %', 
                        v_stock_anterior, NEW.cantidad;
    END IF;
    
    -- Calcular nuevo stock
    v_stock_nuevo := v_stock_anterior - NEW.cantidad;
    
    -- Actualizar stock en inventario
    UPDATE inventario
    SET stock_actual = v_stock_nuevo
    WHERE producto_id = NEW.producto_id AND local_id = v_local_id;
    
    -- Registrar movimiento
    INSERT INTO movimientos_inventario (
        producto_id, local_id, tipo_movimiento, cantidad,
        stock_anterior, stock_nuevo, precio_unitario, costo_total, venta_id, created_by
    ) VALUES (
        NEW.producto_id,
        v_local_id,
        'VENTA',
        NEW.cantidad,
        v_stock_anterior,
        v_stock_nuevo,
        NEW.precio_unitario,
        NEW.total,
        NEW.venta_id,
        (SELECT created_by FROM ventas WHERE id = NEW.venta_id)
    );
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_stock_venta
    AFTER INSERT ON detalle_ventas
    FOR EACH ROW EXECUTE FUNCTION actualizar_stock_en_venta();

-- Funcion: Generar numero de venta automatico
CREATE OR REPLACE FUNCTION generar_numero_venta()
RETURNS TRIGGER AS $$
DECLARE
    v_correlativo INTEGER;
    v_anio VARCHAR(4);
    v_mes VARCHAR(2);
BEGIN
    IF NEW.numero_venta IS NULL OR NEW.numero_venta = '' THEN
        v_anio := TO_CHAR(CURRENT_DATE, 'YYYY');
        v_mes := TO_CHAR(CURRENT_DATE, 'MM');
        
        SELECT COALESCE(MAX(CAST(SUBSTRING(numero_venta FROM 11) AS INTEGER)), 0) + 1
        INTO v_correlativo
        FROM ventas
        WHERE numero_venta LIKE 'V-' || v_anio || '-' || v_mes || '-%';
        
        NEW.numero_venta := 'V-' || v_anio || '-' || v_mes || '-' || 
                           LPAD(v_correlativo::TEXT, 5, '0');
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_generar_numero_venta
    BEFORE INSERT ON ventas
    FOR EACH ROW EXECUTE FUNCTION generar_numero_venta();
