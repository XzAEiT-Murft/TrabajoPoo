IF DB_ID('delicias') IS NULL
    CREATE DATABASE delicias;
GO
USE delicias;
GO

CREATE TABLE clientes (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    nombre NVARCHAR(255) NOT NULL,
    correo NVARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE platillos (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    nombre NVARCHAR(255) NOT NULL UNIQUE,
    precio FLOAT NOT NULL CHECK (precio > 0)
);

CREATE TABLE pedidos (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    fecha DATETIME2 NOT NULL DEFAULT SYSUTCDATETIME(),
    CONSTRAINT FK_pedidos_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id)
);

CREATE TABLE pedido_detalle (
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    platillo_id BIGINT NOT NULL,
    cantidad INT NOT NULL CHECK (cantidad > 0),
    CONSTRAINT FK_detalle_pedido FOREIGN KEY (pedido_id) REFERENCES pedidos(id) ON DELETE CASCADE,
    CONSTRAINT FK_detalle_platillo FOREIGN KEY (platillo_id) REFERENCES platillos(id)
);