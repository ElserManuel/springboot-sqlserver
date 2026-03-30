-- ============================================================
-- PASO 1: CREAR Y USAR LA BASE DE DATOS
-- ============================================================
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'ElserDB')
BEGIN
    CREATE DATABASE ElserDB;
    PRINT '✔ Base de datos ElserDB creada.';
END
ELSE
    PRINT '⚠ Base de datos ElserDB ya existe.';
GO

USE ElserDB;
GO

-- ============================================================
-- PASO 2: ELIMINAR TABLAS SI EXISTEN (orden por dependencias)
-- ============================================================
IF OBJECT_ID('dbo.DetallePedido', 'U') IS NOT NULL DROP TABLE dbo.DetallePedido;
IF OBJECT_ID('dbo.Pedido',        'U') IS NOT NULL DROP TABLE dbo.Pedido;
IF OBJECT_ID('dbo.Producto',      'U') IS NOT NULL DROP TABLE dbo.Producto;
IF OBJECT_ID('dbo.Cliente',       'U') IS NOT NULL DROP TABLE dbo.Cliente;
IF OBJECT_ID('dbo.Proveedor',     'U') IS NOT NULL DROP TABLE dbo.Proveedor;
IF OBJECT_ID('dbo.Categoria',     'U') IS NOT NULL DROP TABLE dbo.Categoria;
GO

-- ============================================================
-- TABLA 1: CATEGORIA
-- ============================================================
CREATE TABLE dbo.Categoria (
    id_categoria   INT           IDENTITY(1,1) PRIMARY KEY,
    nombre         VARCHAR(100)  NOT NULL UNIQUE,
    descripcion    VARCHAR(255)  NULL,
    activo         BIT           NOT NULL DEFAULT 1,
    fecha_creacion DATETIME      NOT NULL DEFAULT GETDATE()
);
GO

-- ============================================================
-- TABLA 2: PROVEEDOR
-- ============================================================
CREATE TABLE dbo.Proveedor (
    id_proveedor   INT           IDENTITY(1,1) PRIMARY KEY,
    ruc            VARCHAR(20)   NOT NULL UNIQUE,
    razon_social   VARCHAR(150)  NOT NULL,
    contacto       VARCHAR(100)  NULL,
    telefono       VARCHAR(20)   NULL,
    email          VARCHAR(100)  NULL,
    direccion      VARCHAR(255)  NULL,
    activo         BIT           NOT NULL DEFAULT 1,
    fecha_creacion DATETIME      NOT NULL DEFAULT GETDATE()
);
GO

-- ============================================================
-- TABLA 3: PRODUCTO
-- ============================================================
CREATE TABLE dbo.Producto (
    id_producto    INT             IDENTITY(1,1) PRIMARY KEY,
    id_categoria   INT             NOT NULL,
    id_proveedor   INT             NULL,
    codigo         VARCHAR(50)     NOT NULL UNIQUE,
    nombre         VARCHAR(150)    NOT NULL,
    descripcion    VARCHAR(500)    NULL,
    precio_compra  DECIMAL(10,2)   NOT NULL DEFAULT 0,
    precio_venta   DECIMAL(10,2)   NOT NULL,
    stock          INT             NOT NULL DEFAULT 0,
    stock_minimo   INT             NOT NULL DEFAULT 5,
    unidad_medida  VARCHAR(30)     NOT NULL DEFAULT 'UNIDAD',
    imagen_url     VARCHAR(300)    NULL,
    activo         BIT             NOT NULL DEFAULT 1,
    fecha_creacion DATETIME        NOT NULL DEFAULT GETDATE(),
    fecha_actualizacion DATETIME   NULL,

    CONSTRAINT FK_Producto_Categoria FOREIGN KEY (id_categoria)
        REFERENCES dbo.Categoria(id_categoria),
    CONSTRAINT FK_Producto_Proveedor FOREIGN KEY (id_proveedor)
        REFERENCES dbo.Proveedor(id_proveedor),
    CONSTRAINT CK_Precio_Venta CHECK (precio_venta >= 0),
    CONSTRAINT CK_Stock        CHECK (stock >= 0)
);
GO

-- ============================================================
-- TABLA 4: CLIENTE
-- ============================================================
CREATE TABLE dbo.Cliente (
    id_cliente     INT           IDENTITY(1,1) PRIMARY KEY,
    tipo_doc       VARCHAR(10)   NOT NULL DEFAULT 'DNI',  -- DNI, RUC, CE
    num_doc        VARCHAR(20)   NOT NULL UNIQUE,
    nombre         VARCHAR(100)  NOT NULL,
    apellido       VARCHAR(100)  NULL,
    email          VARCHAR(100)  NULL,
    telefono       VARCHAR(20)   NULL,
    direccion      VARCHAR(255)  NULL,
    ciudad         VARCHAR(80)   NULL,
    activo         BIT           NOT NULL DEFAULT 1,
    fecha_creacion DATETIME      NOT NULL DEFAULT GETDATE(),

    CONSTRAINT CK_TipoDoc CHECK (tipo_doc IN ('DNI','RUC','CE','PASAPORTE'))
);
GO

-- ============================================================
-- TABLA 5: PEDIDO (cabecera de venta)
-- ============================================================
CREATE TABLE dbo.Pedido (
    id_pedido      INT             IDENTITY(1,1) PRIMARY KEY,
    id_cliente     INT             NOT NULL,
    numero_pedido  VARCHAR(20)     NOT NULL UNIQUE,
    fecha_pedido   DATETIME        NOT NULL DEFAULT GETDATE(),
    estado         VARCHAR(20)     NOT NULL DEFAULT 'PENDIENTE',
    -- PENDIENTE | CONFIRMADO | ENVIADO | ENTREGADO | CANCELADO
    subtotal       DECIMAL(12,2)   NOT NULL DEFAULT 0,
    descuento      DECIMAL(12,2)   NOT NULL DEFAULT 0,
    igv            DECIMAL(12,2)   NOT NULL DEFAULT 0,   -- 18%
    total          DECIMAL(12,2)   NOT NULL DEFAULT 0,
    metodo_pago    VARCHAR(30)     NULL,   -- EFECTIVO, TARJETA, TRANSFERENCIA
    observacion    VARCHAR(500)    NULL,
    fecha_creacion DATETIME        NOT NULL DEFAULT GETDATE(),

    CONSTRAINT FK_Pedido_Cliente FOREIGN KEY (id_cliente)
        REFERENCES dbo.Cliente(id_cliente),
    CONSTRAINT CK_Estado_Pedido CHECK (
        estado IN ('PENDIENTE','CONFIRMADO','ENVIADO','ENTREGADO','CANCELADO')
    )
);
GO

-- ============================================================
-- TABLA 6: DETALLE PEDIDO (líneas de venta)
-- ============================================================
CREATE TABLE dbo.DetallePedido (
    id_detalle     INT             IDENTITY(1,1) PRIMARY KEY,
    id_pedido      INT             NOT NULL,
    id_producto    INT             NOT NULL,
    cantidad       INT             NOT NULL,
    precio_unit    DECIMAL(10,2)   NOT NULL,
    descuento      DECIMAL(10,2)   NOT NULL DEFAULT 0,
    subtotal       AS (cantidad * precio_unit - descuento) PERSISTED,

    CONSTRAINT FK_Detalle_Pedido   FOREIGN KEY (id_pedido)
        REFERENCES dbo.Pedido(id_pedido),
    CONSTRAINT FK_Detalle_Producto FOREIGN KEY (id_producto)
        REFERENCES dbo.Producto(id_producto),
    CONSTRAINT CK_Cantidad CHECK (cantidad > 0),
    CONSTRAINT CK_PrecioUnit CHECK (precio_unit >= 0)
);
GO

-- ============================================================
-- PASO 3: ÍNDICES para mejorar rendimiento
-- ============================================================
CREATE INDEX IX_Producto_Categoria  ON dbo.Producto(id_categoria);
CREATE INDEX IX_Producto_Proveedor  ON dbo.Producto(id_proveedor);
CREATE INDEX IX_Producto_Codigo     ON dbo.Producto(codigo);
CREATE INDEX IX_Pedido_Cliente      ON dbo.Pedido(id_cliente);
CREATE INDEX IX_Pedido_Fecha        ON dbo.Pedido(fecha_pedido);
CREATE INDEX IX_Pedido_Estado       ON dbo.Pedido(estado);
CREATE INDEX IX_Detalle_Pedido      ON dbo.DetallePedido(id_pedido);
GO

-- ============================================================
-- PASO 4: TRIGGER — actualizar stock al confirmar pedido
-- ============================================================
CREATE TRIGGER trg_ActualizarStock
ON dbo.Pedido
AFTER UPDATE
AS
BEGIN
    SET NOCOUNT ON;

    IF UPDATE(estado)
    BEGIN
        -- Descontar stock cuando pasa a CONFIRMADO
        IF EXISTS (SELECT 1 FROM inserted WHERE estado = 'CONFIRMADO')
        BEGIN
            UPDATE p
            SET p.stock = p.stock - d.cantidad
            FROM dbo.Producto p
            INNER JOIN dbo.DetallePedido d ON d.id_producto = p.id_producto
            INNER JOIN inserted i          ON i.id_pedido   = d.id_pedido
            WHERE i.estado = 'CONFIRMADO';
        END

        -- Devolver stock si se CANCELA
        IF EXISTS (SELECT 1 FROM inserted i
                   INNER JOIN deleted  d ON d.id_pedido = i.id_pedido
                   WHERE i.estado = 'CANCELADO' AND d.estado = 'CONFIRMADO')
        BEGIN
            UPDATE p
            SET p.stock = p.stock + d.cantidad
            FROM dbo.Producto p
            INNER JOIN dbo.DetallePedido d  ON d.id_producto = p.id_producto
            INNER JOIN inserted i           ON i.id_pedido   = d.id_pedido
            WHERE i.estado = 'CANCELADO';
        END
    END
END;
GO

-- ============================================================
-- PASO 5: DATA DE PRUEBA
-- ============================================================

-- Categorias
INSERT INTO dbo.Categoria (nombre, descripcion) VALUES
('Electrónica',     'Dispositivos y accesorios electrónicos'),
('Ropa',            'Prendas de vestir para toda la familia'),
('Alimentos',       'Productos alimenticios y bebidas'),
('Hogar',           'Artículos para el hogar y decoración'),
('Deportes',        'Equipos y ropa deportiva');

-- Proveedores
INSERT INTO dbo.Proveedor (ruc, razon_social, contacto, telefono, email) VALUES
('20100001111', 'Tech Distribuciones SAC',   'Juan Pérez',    '999111222', 'ventas@techdist.com'),
('20100002222', 'Moda Import EIRL',          'Ana Torres',    '999333444', 'pedidos@modaimport.com'),
('20100003333', 'Alimentos del Sur SAC',     'Carlos Ríos',   '999555666', 'ventas@alimentossur.com');

-- Productos
INSERT INTO dbo.Producto (id_categoria, id_proveedor, codigo, nombre, precio_compra, precio_venta, stock, stock_minimo) VALUES
(1, 1, 'ELEC-001', 'Laptop HP 15 pulgadas',        2500.00, 3200.00, 10, 2),
(1, 1, 'ELEC-002', 'Mouse Inalámbrico Logitech',     35.00,   59.90, 50, 10),
(1, 1, 'ELEC-003', 'Teclado Mecánico RGB',           80.00,  130.00, 30, 5),
(2, 2, 'ROPA-001', 'Polo Algodón Talla M',            15.00,   35.00, 100, 20),
(2, 2, 'ROPA-002', 'Jeans Clásico Azul',              45.00,   89.90, 60, 10),
(3, 3, 'ALIM-001', 'Arroz Extra 5kg',                  12.00,   18.50, 200, 50),
(3, 3, 'ALIM-002', 'Aceite Vegetal 1L',                 8.00,   13.00, 150, 30),
(4, 1, 'HOGAR-001','Juego de Sábanas 2 plazas',        40.00,   75.00, 40, 8),
(5, 2, 'DEP-001',  'Zapatillas Running Nike T42',     120.00,  199.90, 25, 5);

-- Clientes
INSERT INTO dbo.Cliente (tipo_doc, num_doc, nombre, apellido, email, telefono, ciudad) VALUES
('DNI', '12345678', 'Luis',    'Gómez Rivas',    'luis.gomez@email.com',    '987654321', 'Lima'),
('DNI', '87654321', 'María',   'López Castro',   'maria.lopez@email.com',   '912345678', 'Arequipa'),
('RUC', '20200001234', 'Empresa ABC SAC', NULL,  'compras@empresaabc.com',  '014445566', 'Lima'),
('DNI', '11223344', 'Carlos',  'Torres Díaz',    'carlos.torres@email.com', '956789012', 'Trujillo');

-- Pedidos y detalles de prueba
INSERT INTO dbo.Pedido (id_cliente, numero_pedido, estado, metodo_pago) VALUES
(1, 'PED-0001', 'ENTREGADO', 'EFECTIVO'),
(2, 'PED-0002', 'CONFIRMADO','TARJETA'),
(3, 'PED-0003', 'PENDIENTE', 'TRANSFERENCIA');

INSERT INTO dbo.DetallePedido (id_pedido, id_producto, cantidad, precio_unit) VALUES
(1, 1, 1, 3200.00),
(1, 2, 2,   59.90),
(2, 4, 3,   35.00),
(2, 5, 1,   89.90),
(3, 6, 5,   18.50),
(3, 7, 3,   13.00);

-- Actualizar totales de pedidos
UPDATE dbo.Pedido SET
    subtotal = (SELECT SUM(subtotal) FROM dbo.DetallePedido WHERE id_pedido = 1),
    igv      = (SELECT SUM(subtotal) FROM dbo.DetallePedido WHERE id_pedido = 1) * 0.18,
    total    = (SELECT SUM(subtotal) FROM dbo.DetallePedido WHERE id_pedido = 1) * 1.18
WHERE id_pedido = 1;

UPDATE dbo.Pedido SET
    subtotal = (SELECT SUM(subtotal) FROM dbo.DetallePedido WHERE id_pedido = 2),
    igv      = (SELECT SUM(subtotal) FROM dbo.DetallePedido WHERE id_pedido = 2) * 0.18,
    total    = (SELECT SUM(subtotal) FROM dbo.DetallePedido WHERE id_pedido = 2) * 1.18
WHERE id_pedido = 2;

UPDATE dbo.Pedido SET
    subtotal = (SELECT SUM(subtotal) FROM dbo.DetallePedido WHERE id_pedido = 3),
    igv      = (SELECT SUM(subtotal) FROM dbo.DetallePedido WHERE id_pedido = 3) * 0.18,
    total    = (SELECT SUM(subtotal) FROM dbo.DetallePedido WHERE id_pedido = 3) * 1.18
WHERE id_pedido = 3;
GO

-- ============================================================
-- PASO 6: VERIFICACION FINAL
-- ============================================================
PRINT '========================================';
PRINT '  VERIFICACION DE TABLAS CREADAS';
PRINT '========================================';

SELECT
    t.name        AS Tabla,
    COUNT(c.name) AS Columnas,
    p.rows        AS Registros
FROM sys.tables t
JOIN sys.columns c ON c.object_id = t.object_id
JOIN sys.partitions p ON p.object_id = t.object_id AND p.index_id <= 1
GROUP BY t.name, p.rows
ORDER BY t.name;
GO

PRINT '✔ Script ejecutado correctamente.';
PRINT '  Base de datos: ElserDB';
PRINT '  Tablas: Categoria, Proveedor, Producto, Cliente, Pedido, DetallePedido';
GO