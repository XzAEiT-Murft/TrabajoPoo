# Delicias Gourmet — Sistema de Gestión de Menú y Pedidos (Escritorio)

Aplicación de escritorio en **Java + JavaFX + JPA (EclipseLink)** para gestionar **clientes**, **platillos** y **pedidos** del restaurante **“Delicias Gourmet”**.  
Proyecto **sin Maven/Gradle**, preparado para **VS Code** con librerías en `lib/`.

---

## 🧩 Características

- **CRUD de Clientes** (nombre y correo, valida correo único).
- **CRUD de Platillos** (nombre único, precio > 0).
- **Gestión de Pedidos**: crear pedido para un cliente con múltiples platillos y cantidades.
- **Consultas**:
  - Pedidos por cliente.
  - Detalles de un pedido (platillos, cantidades, subtotales).
  - **Total** por pedido (cálculo en la entidad).
- **Eliminaciones**:
  - Borrado de pedidos (con borrado en cascada de detalles por FK).
- **Manejo de errores**:
  - No permite pedidos sin platillos.
  - No permite cantidades ≤ 0.
- **UI**:
  - En la vista de **Pedidos** se muestran **nombres** de cliente y platillos (no solo IDs), usando `JOIN FETCH` y `cellValueFactory`.

---

## 🛠️ Tecnologías

- **Java** 22 (compatible 17+)
- **JavaFX** 24.x
- **Jakarta Persistence (JPA 3.1)** con **EclipseLink 4.0.2**
- **Base de datos**: **SQL Server** (2019/2022)
- **IDE**: **Visual Studio Code** (sin Maven/Gradle)

> *Nota*: EclipseLink puede mostrar el aviso *“Java SE '22' is not fully supported yet”*. Es **informativo**, no bloquea.

---

## 🗂️ Estructura del proyecto

TrabajoPoo/
database/
delicias.sql
src/
App.java
META-INF/
persistence.xml
models/
Cliente.java
Platillo.java
Pedido.java
PedidoDetalle.java
controllers/
ClienteController.java
PlatilloController.java
PedidoController.java
repositories/
PedidoRepository.java
utils/
JPAUtil.java
views/
ClienteView.java
PlatilloView.java
PedidoView.java
styles/
main.css
lib/
eclipselink-4.0.2.jar
jakarta.persistence-api-3.1.0.jar
jakarta.activation-api-2.1.1.jar
jakarta.xml.bind-api-4.0.0.jar
mssql-jdbc-12.10.1.jre11.jar
javafx.base.jar
javafx.controls.jar
javafx.fxml.jar
javafx.graphics.jar
(y demás .jar de JavaFX)
.vscode/
settings.json
launch.json
README.md


---

## 🗄️ Modelo relacional (SQL Server)

```sql
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

USE delicias;
GO
INSERT INTO clientes (nombre, correo) VALUES
('Ana Pérez','ana@delicias.test'),
('Luis García','luis@delicias.test'),
('María López','maria@delicias.test'),
('Carlos Ruiz','carlos@delicias.test'),
('Sofía Torres','sofia@delicias.test');
GO

INSERT INTO platillos (nombre, precio) VALUES
('Enchiladas Verdes',120.00),
('Tacos al Pastor',90.00),
('Sopa Azteca',75.00),
('Chiles en Nogada',180.00),
('Quesadillas',60.00);
GO

DECLARE @c1 BIGINT = (SELECT id FROM clientes WHERE correo='ana@delicias.test');
DECLARE @c2 BIGINT = (SELECT id FROM clientes WHERE correo='luis@delicias.test');
DECLARE @c3 BIGINT = (SELECT id FROM clientes WHERE correo='maria@delicias.test');
DECLARE @c5 BIGINT = (SELECT id FROM clientes WHERE correo='sofia@delicias.test');

DECLARE @p1 BIGINT = (SELECT id FROM platillos WHERE nombre='Enchiladas Verdes');
DECLARE @p2 BIGINT = (SELECT id FROM platillos WHERE nombre='Tacos al Pastor');
DECLARE @p3 BIGINT = (SELECT id FROM platillos WHERE nombre='Sopa Azteca');
DECLARE @p4 BIGINT = (SELECT id FROM platillos WHERE nombre='Chiles en Nogada');
DECLARE @p5 BIGINT = (SELECT id FROM platillos WHERE nombre='Quesadillas');

INSERT INTO pedidos (cliente_id) VALUES (@c1); DECLARE @o1 BIGINT = SCOPE_IDENTITY();
INSERT INTO pedido_detalle (pedido_id,platillo_id,cantidad) VALUES (@o1,@p1,2),(@o1,@p3,1);

INSERT INTO pedidos (cliente_id) VALUES (@c2); DECLARE @o2 BIGINT = SCOPE_IDENTITY();
INSERT INTO pedido_detalle (pedido_id,platillo_id,cantidad) VALUES (@o2,@p2,3),(@o2,@p5,1);

INSERT INTO pedidos (cliente_id) VALUES (@c1); DECLARE @o3 BIGINT = SCOPE_IDENTITY();
INSERT INTO pedido_detalle (pedido_id,platillo_id,cantidad) VALUES (@o3,@p4,1);

INSERT INTO pedidos (cliente_id) VALUES (@c3); DECLARE @o4 BIGINT = SCOPE_IDENTITY();
INSERT INTO pedido_detalle (pedido_id,platillo_id,cantidad) VALUES (@o4,@p5,2),(@o4,@p3,1);

INSERT INTO pedidos (cliente_id) VALUES (@c5); DECLARE @o5 BIGINT = SCOPE_IDENTITY();
INSERT INTO pedido_detalle (pedido_id,platillo_id,cantidad) VALUES (@o5,@p1,1),(@o5,@p2,2);
GO

```

🧱 Entidades (JPA) — resumen

models.Cliente: id, nombre, correo (único).

models.Platillo: id, nombre (único), precio.

models.Pedido: id, cliente (ManyToOne), fecha, detalles (OneToMany, cascade=ALL, orphanRemoval=true).

getTotal() → suma subtotales de detalles.

models.PedidoDetalle: id, pedido (ManyToOne), platillo (ManyToOne), cantidad.

getSubtotal() → platillo.precio * cantidad.

Consulta para listar pedidos con nombres ya cargados:
// repositories/PedidoRepository.java
return em.createQuery(
  "SELECT DISTINCT p FROM Pedido p " +
  "JOIN FETCH p.cliente " +
  "LEFT JOIN FETCH p.detalles d " +
  "LEFT JOIN FETCH d.platillo " +
  "ORDER BY p.id DESC", Pedido.class
).getResultList();

🔌 Configuración de JPA (META-INF/persistence.xml)
<?xml version="1.0" encoding="UTF-8"?>
<persistence xmlns="https://jakarta.ee/xml/ns/persistence" version="3.1">
  <persistence-unit name="DeliciasPU">
    <class>models.Cliente</class>
    <class>models.Platillo</class>
    <class>models.Pedido</class>
    <class>models.PedidoDetalle</class>
    <properties>
      <property name="jakarta.persistence.jdbc.driver" value="com.microsoft.sqlserver.jdbc.SQLServerDriver"/>
      <property name="jakarta.persistence.jdbc.url" value="jdbc:sqlserver://localhost:1433;databaseName=delicias;encrypt=false;trustServerCertificate=true"/>
      <property name="jakarta.persistence.jdbc.user" value="sa"/>
      <property name="jakarta.persistence.jdbc.password" value="TU_PASSWORD"/>
      <property name="jakarta.persistence.schema-generation.database.action" value="none"/>
      <property name="eclipselink.logging.level" value="FINE"/>
      <property name="eclipselink.logging.parameters" value="true"/>
    </properties>
  </persistence-unit>
</persistence>

