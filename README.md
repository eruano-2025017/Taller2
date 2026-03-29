## 1. Visión del Proyecto
**KinalApp** Lo que realizamos fue una API REST construida con tecnologías modernas del ecosistema Java. Su función principal es administrar el ciclo de ventas de un negocio, facilitando la ejecución de operaciones CRUD (Crear, Leer, Actualizar y Eliminar) sobre entidades fundamentales como clientes, productos, usuarios y las transacciones de venta.

La aplicación destaca por su **integridad referencial**, asegurando que cada venta esté vinculada a un cliente existente y sea procesada por un usuario autorizado en el sistema.

---

## 2. Especificaciones Técnicas
* **Lenguaje**: Java 21 (LTS).
* **Framework**: Spring Boot 4.0.2.
* **Persistencia**: Spring Data JPA con motor **MySQL**.
* **Gestión de Dependencias**: Maven (POM XML).
* **Estandarización de Datos**: Implementación de tipos **Long** en todos los identificadores (ID).

---

## 3. Guía de Configuración y Despliegue

### Requisitos de Entorno
* **Java Development Kit (JDK) 21**.
* **Servidor MySQL** (Puerto 3306).
* **Maven 3.9+**.

### Pasos de Instalación
* ** 1. Clonar repositorio: `https://github.com/eruano-2025017/Taller2.git`
* ** 2. Abrir Intellij IDEA.
* **3. Abrir la carpeta que clonó.
* ** 4. Abrir MySQL en su ordenador.
* ** 5. Ingresar a la instancia activa en MySQL.
* ** 6. Regresar a Intellij IDEA.
* ** 7. Dirigirse a la carpeta `src\main\java\com\estebanruano`.
* ** 8. Dirigirse a `KinalAppApplication` y ejecutar la aplicación.
* ** 9. Abrir la carpeta `resources/application.properties`.
* ** 10. Verificar qué puerto está utilizando la aplicación.
* ** 11. Abrir el navegador y poner el puerto: `http://localhost:8083/clientes`.



---

## 4. Diccionario de Endpoints (API Reference)

El sistema utiliza rutas normalizadas para cada recurso, diferenciando las operaciones mediante verbos HTTP (GET, POST, PUT, DELETE).

###  Módulo de Clientes (`/clientes`)
* `GET /clientes`: Lista todos los clientes registrados.
* `POST /clientes`: Registra un nuevo cliente.
* `GET /clientes/estado`: Filtra clientes con estado activo.
* `GET /clientes/{id}`: Busca un cliente por su ID (**Long**).
* `PUT /clientes/{id}`: Actualiza los datos de un cliente.
* `DELETE /clientes/{id}`: Baja lógica o física de un cliente.

###  Módulo de Usuarios (`/usuarios`)
* `GET /usuarios`: Lista todos los usuarios registrados.
* `POST /usuarios`: Registra un nuevo usuarios.
* `GET /usuarios/estado`: Filtra usuarios con estado activo.
* `GET /usuarios/{codigoUsu}`: Busca un usuarios por su ID (**Long**).
* `PUT /usuarios/{codigoUsu}`: Actualiza los datos de un usuarios.
* `DELETE /usuarios/{codigoUsu}`: Baja lógica o física de un usuarios.

###  Módulo de Productos (`/productos`)
* `GET /productos`: Lista todos los productos registrados.
* `POST /productos`: Registra un nuevo productos.
* `GET /productos/estado`: Filtra productos con estado activo.
* `GET /productos/{codigoPro}`: Busca un productos por su ID (**Long**).
* `PUT /productos/{codigoPro}`: Actualiza los datos de un productos.
* `DELETE /productos/{codigoPro}`: Baja lógica o física de un productos.

###  Módulo de Ventas (`/ventas`)
* `GET /ventas`: Lista todos los ventas registrados.
* `POST /ventas`: Registra un nuevo ventas.
* `GET /ventas/estado`: Filtra ventas con estado activo.
* `GET /ventas/{codigoVen}`: Busca un ventas por su ID (**Long**).
* `PUT /ventas/{codigoVen}`: Actualiza los datos de un ventas.
* `DELETE /ventas/{codigoVen}`: Baja lógica o física de un ventas.

###  Módulo de Detalles (`/detalleVentas`)
* `GET /detalleVentas`: Lista todos los clientes registrados.
* `POST /detalleVentas`: Registra un nuevo cliente.
* `GET /detalleVentas/{codigoDeVe}`: Busca un detalleVentas por su ID (**Long**).
* `PUT /detalleVentas/{codigoDeVe}`: Actualiza los datos de un cliente.
* `DELETE /detalleVentas/{codigoDeVe}`: Baja lógica o física de un cliente.