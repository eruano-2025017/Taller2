#KinalApp

Documentación del Proyecto
Lo que realizamos fue una API REST construida con tecnologías modernas del ecosistema Java. Su función principal es administrar el ciclo de ventas de un negocio, facilitando la ejecución de operaciones CRUD (Crear, Leer, Actualizar y Eliminar) sobre entidades fundamentales como clientes, productos, usuarios y las transacciones de venta.

Análisis de los Componentes
Stack Tecnológico: Emplea las versiones más recientes y estables del mercado. Java 21 proporciona las últimas mejoras de rendimiento, mientras que Spring Boot 4 (que en el contexto actual de 2026 es el estándar) simplifica la configuración automática y el despliegue rápido.
Arquitectura de Datos: Se respalda en MySQL para el almacenamiento persistente y Maven para gestionar todas las librerías necesarias.
Estructura de Endpoints: La aplicación sigue una lógica de recursos muy definida:
Gestión de Inventario: Control de productos y supervisión de stock.
Gestión de Ventas: Registro detallado de transacciones (Venta y DetalleVenta), lo cual indica que puede manejar múltiples productos por cada factura.
Seguridad/Control: Gestión de usuarios y estados de actividad (filtros para visualizar solo registros "activos").
Flujo de Trabajo (Instalación)
El proceso descrito es el estándar para un desarrollador:
Preparación del entorno: Verificar que el motor de base de datos y el kit de desarrollo (JDK) estén listos.
Sincronización: Clonar el código desde el repositorio de GitHub.
Configuración: Es fundamental el paso de revisar el archivo application.properties, ya que ahí se define la conexión a la base de datos y el puerto de red (en este caso, el 8081).
Pruebas: Se recomienda el uso de Postman o el navegador para interactuar con los datos a través de las URLs (por ejemplo, para visualizar la lista de clientes).
Una pequeña observación técnica
En la sección de Endpoints, se menciona que se utiliza /{dpi} para buscar, eliminar y actualizar.
Dato curioso: Aunque se emplea la etiqueta {dpi} (que es el identificador único en Guatemala), en el desarrollo de software se suele llamar genéricamente {id}. Es interesante que esté personalizado para el contexto local.
Diferenciación de Métodos: Para que esos 3 endpoints funcionen en la misma URL (/{dpi}), Spring Boot utiliza diferentes métodos HTTP: GET para buscar, DELETE para eliminar y PUT/PATCH para actualizar.

Tecnologias utilizadas
Java 21
Spring Boot 4.0.2
Maven (Gestor de dependencias)
MySQL (Sistema gestor de Base de Datos)

Requisitos Previos
Antes de ejecutar la aplicación debe tener instalado:
JDK 17 o superior
Maven instalado
Una instancia activa en MySQL
Instalaciones opcionales

Postman

Instalación y Ejecución

Clonar repositorio https://github.com/eruano-2025017/Taller2.git
Abrir Intellij IDEA.
Abrir la carpeta que clono.
Abrir MySQL en su ordenador.
Ingresar a la instancia activa en MySQL.
Regresar a Intellij IDEA.
Dirigirse a la carpeta "src\main\java\com\estebanruano".
Dirirgirse a KinalAppApplication y ejecutar la aplicación.
Abrir la carpeta "resources/application.properties".
Verificar que puerto esta utilizando la aplicación.
Abrir el navegador y poner el puerto http://localhost:8083/clientes.

Endpoints


Cliente:
"/clientes": Esto nos lista los clientes y agrega el cliente.
"/estado": Esto lista los clientes que están activos.
"/{dpi}": Esto busca el cliente mediante él id.
"/{dpi}": Esto elimina el cliente mediante él id.
"/{dpi}": Esto actualiza el cliente mediante él id.

Usuario:
"/usuarios": Esto nos lista los usuarios y agrega el usuario.
"/estado": Esto lista los usuarios que están activos.
"/{codigoUsu}": Esto busca el usuario mediante él id.
"/{codigoUsu}": Esto elimina el usuario mediante él id.
"/{codigoUsu}": Esto actualiza el usuario mediante él id.

Producto:
"/productos": Esto nos lista los productos y agrega el producto.
"/estado": Esto lista los productos que están activos.
"/{codigoPro}": Esto busca el producto mediante él id.
"/{codigoPro}": Esto elimina el producto mediante él id.
"/{codigoPro}": Esto actualiza el producto mediante él id.
"/stock": Esto lista el nombre del producto y la cantidad que hay en stock.

Venta:
"/ventas": Esto nos lista las ventas y agrega la venta.
"/estado": Esto lista las ventas que están activos.
"/{codigoVen}": Esto busca la venta mediante él id.
"/{codigoVen}": Esto elimina la venta mediante él id.
"/{codigoVen}": Esto actualiza la venta mediante él id.

DetalleVenta:
"/detalleVentas": Esto nos lista los detalles de venta y agrega el detalle venta.
"/{codigoDeVe}": Esto busca los detalles de venta mediante él id.
"/{codigoDeVe}": Esto elimina los detalles de venta mediante él id.
"/{codigoDeVe}": Esto actualiza los detalles de venta mediante él id.