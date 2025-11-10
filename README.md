# API de Pagos con Tarjetas de Crédito

**Trabajo Final - DISEÑO DE BASES DE DATOS - MG IS - UNLP - 2025**

Sistema RESTful de gestión de pagos con tarjetas de crédito desarrollado con Spring Boot 3, Spring Data MongoDB y MongoDB.

## 📋 Descripción

Este sistema permite gestionar de forma integral:
- **Bancos** y sus clientes (titulares de tarjetas)
- **Tarjetas de crédito** con validación de emisión y vencimiento
- **Compras** (en un solo pago o en cuotas)
- **Promociones** (descuentos y financiaciones especiales)
- **Generación automática de pagos mensuales** con cálculo de totales

## 🛠️ Tecnologías Utilizadas

- **Java 21** con Virtual Threads
- **Spring Boot 3.5.7**
- **Spring Data MongoDB**
- **MongoDB 7.0+**
- **Flapdoodle Embedded MongoDB** (para tests)
- **Lombok** para reducción de boilerplate
- **Maven** como gestor de dependencias
- **SpringDoc OpenAPI 3.0** (Swagger UI) para documentación interactiva
- **JUnit 5** y **AssertJ** para testing

## 📦 Requisitos Previos

- **JDK 21** o superior
- **Maven 3.6+**
- **MongoDB 7.0+** instalado y en ejecución
- **IDE** (IntelliJ IDEA, Eclipse, VS Code con extensiones Java)

## ⚙️ Configuración e Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/gmmaunas/api-pagos-tarjetas.git
cd api-pagos-tarjetas
```

### 2. Configurar MongoDB

#### Opción A: Instalación Local

1. Descargar e instalar MongoDB Community Server desde https://www.mongodb.com/try/download/community
2. Iniciar el servicio MongoDB:

**Windows:**
```bash
net start MongoDB
```

**Linux/Mac:**
```bash
sudo systemctl start mongod
```

3. Verificar que MongoDB está corriendo:

**Linux/Mac:**
```bash
mongosh
```

#### Opción B: Docker

```bash
docker run -d -p 27017:27017 --name mongodb mongo:7.0
```

### 3. Configurar conexión (Opcional)

La aplicación se conecta por defecto a `mongodb://localhost:27017/api_pagos_tarjetas`.

Para cambiar la configuración, editar `src/main/resources/application.yml`:

```properties
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/api_pagos_tarjetas
      # Con autenticación:
      # uri: mongodb://username:password@localhost:27017/api_pagos_tarjetas
```

### 4. Compilar y ejecutar

#### Compilar el proyecto
```bash
mvn clean install
```
#### Ejecutar la aplicación
```bash
mvn spring-boot:run
```

La aplicación estará disponible en:

- **API Base URL**: http://localhost:8081/dbd/v2
- **Swagger UI**: http://localhost:8081/dbd/v2/swagger-ui.html
- **OpenAPI Docs**: http://localhost:8081/dbd/v2/api-docs

### 5. Acceder a la documentación interactiva

Una vez que la aplicación esté en ejecución, accede a Swagger UI:

🔗 http://localhost:8081/dbd/v2/swagger-ui.html

Swagger UI te permite:
- 📝 Ver todos los endpoints disponibles organizados por controladores
- 📦 Explorar los modelos de datos (Request/Response DTOs)
- ▶️ Probar los endpoints directamente desde el navegador
- 📊 Ver ejemplos de request/response con validaciones

## 📚 Estructura del Proyecto

```
src/main/java/com/dbd/service/pagos_tarjetas/
├── conf/                          # Configuración de la aplicación
│   ├── OpenApiConfig.java          # Configuración de Swagger/OpenAPI
│   └── GlobalControllerExceptionHandler.java # Manejo global de excepciones
│
├── model/                           # Entidades MongoDB del dominio
│   ├── Banco.java
│   ├── TitularTarjeta.java
│   ├── Tarjeta.java
│   ├── Compra.java                 # Clase abstracta
│   ├── CompraPagoUnico.java
│   ├── CompraCuotas.java
│   ├── Cuota.java
│   ├── Promocion.java              # Clase abstracta
│   ├── Descuento.java
│   ├── Financiacion.java
│   └── Pago.java
│
├── repository/                      # Repositorios Spring Data MongoDB
│   ├── BancoRepository.java
│   ├── TitularTarjetaRepository.java
│   ├── TarjetaRepository.java
│   ├── CompraRepository.java
│   ├── CompraPagoUnicoRepository.java
│   ├── CompraCuotasRepository.java
│   ├── PromocionRepository.java
│   ├── DescuentoRepository.java
│   ├── FinanciacionRepository.java
│   └── PagoRepository.java
│
├── service/                         # Interfaces de servicios
│   ├── IBancoService.java
│   ├── ITitularTarjetaService.java
│   ├── ITarjetaService.java
│   ├── ICompraService.java
│   ├── IPromocionService.java
│   └── IPagoService.java
│
├── service/impl/                    # Implementaciones de servicios
│   ├── BancoServiceImpl.java
│   ├── TitularTarjetaServiceImpl.java
│   ├── TarjetaServiceImpl.java
│   ├── CompraServiceImpl.java
│   ├── PromocionServiceImpl.java
│   └── PagoServiceImpl.java
│
├── rest/                            # Capa REST
    ├── controller/                  # Controladores REST
    │   ├── BancoController.java
    │   ├── TitularTarjetaController.java
    │   ├── TarjetaController.java
    │   ├── CompraController.java
    │   ├── PromocionController.java
    │   └── PagoController.java
    │
    ├── request/                     # DTOs de entrada
    │   ├── BancoRequest.java
    │   ├── TitularTarjetaRequest.java
    │   ├── TarjetaRequest.java
    │   ├── CompraPagoUnicoRequest.java
    │   ├── CompraCuotasRequest.java
    │   ├── DescuentoRequest.java
    │   ├── FinanciacionRequest.java
    │   ├── PagoRequest.java
    │   └── EditarFechasVencimientoRequest.java
    │
    ├── response/                    # DTOs de salida
    │   ├── BancoResponse.java
    │   ├── TitularTarjetaResponse.java
    │   ├── TarjetaResponse.java
    │   ├── CompraResponse.java
    │   ├── CompraPagoUnicoResponse.java
    │   ├── CompraCuotasResponse.java
    │   ├── CuotaResponse.java
    │   ├── PromocionResponse.java
    │   ├── DescuentoResponse.java
    │   ├── FinanciacionResponse.java
    │   ├── PagoResponse.java
    │   └── ApiErrorResponse.java
    │
    └── mapper/                      # Mappers Entity ↔ DTO
        ├── BancoMapper.java
        ├── TitularTarjetaMapper.java
        ├── TarjetaMapper.java
        ├── CompraMapper.java
        ├── PromocionMapper.java
        └── PagoMapper.java

src/test/java/com/dbd/service/pagos_tarjetas/
├── PagosTarjetasApplicationTests.java
└── ApiPagosTarjetasIntegrationTests.java  # 14 Tests de integración completos
```

## 🔌 API Endpoints

### 🏦 Bancos (/dbd/v2/bancos)

- `POST /bancos` - Crear un banco
- `GET /bancos` - Obtener todos los bancos
- `GET /bancos/{id}` - Obtener banco por ID
- `GET /bancos/cuit/{cuit}` - Obtener banco por CUIT
- `PUT /bancos/{id}` - Actualizar banco
- `DELETE /bancos/{id}` - Eliminar banco
- `GET /bancos/mas-compras` - **Obtener banco con mayor cantidad de compras**
- `GET /bancos/clientes-por-banco` - **Obtener número de clientes por banco**

### 👤 Titulares (/dbd/v2/titulares)

- `POST /titulares` - Crear un titular
- `GET /titulares` - Obtener todos los titulares
- `GET /titulares/{id}` - Obtener titular por ID
- `GET /titulares/cuit/{cuit}` - Obtener titular por CUIT
- `GET /titulares/dni/{dni}` - Obtener titular por DNI
- `GET /titulares/banco/{bancoId}` - Obtener titulares por banco
- `PUT /titulares/{id}` - Actualizar titular
- `DELETE /titulares/{id}` - Eliminar titular
- `GET /titulares/top-compradores?limite=10` - **Top N titulares con mayor monto en compras**

### 💳 Tarjetas (/dbd/v2/tarjetas)

- `POST /tarjetas` - Crear una tarjeta
- `GET /tarjetas` - Obtener todas las tarjetas
- `GET /tarjetas/{id}` - Obtener tarjeta por ID
- `GET /tarjetas/numero/{numero}` - Obtener tarjeta por número
- `GET /tarjetas/titular/{titularId}` - Obtener tarjetas por titular
- `GET /tarjetas/banco/{bancoId}` - Obtener tarjetas por banco
- `PUT /tarjetas/{id}` - Actualizar tarjeta
- `DELETE /tarjetas/{id}` - Eliminar tarjeta
- `GET /tarjetas/emitidas?anios=5` - **Tarjetas emitidas hace más de N años**

### 🛒 Compras (/dbd/v2/compras)

- `POST /compras/pago-unico` - Crear compra en un solo pago
- `POST /compras/cuotas` - Crear compra en cuotas
- `GET /compras` - Obtener todas las compras
- `GET /compras/{id}` - Obtener compra por ID
- `GET /compras/{id}/detalles` - Obtener compra con detalles completos
- `GET /compras/tarjeta/{tarjetaId}` - Obtener compras por tarjeta
- `GET /compras/local-mas-compras` - **Obtener local con mayor cantidad de compras**
- `DELETE /compras/{id}` - Eliminar compra

### 🎁 Promociones (/dbd/v2/promociones)

- `POST /promociones/descuento` - **Agregar promoción de descuento a un banco**
- `POST /promociones/financiacion` - Agregar promoción de financiación
- `GET /promociones` - Obtener todas las promociones
- `GET /promociones/{id}` - Obtener promoción por ID
- `GET /promociones/codigo/{codigo}` - Obtener promoción por código
- `GET /promociones/banco/{bancoId}` - Obtener promociones por banco
- `GET /promociones/local/{cuitTienda}?fechaInicio=YYYY-MM-DD&fechaFin=YYYY-MM-DD` - **Promociones disponibles por local entre fechas**
- `PUT /promociones/{id}` - Actualizar promoción
- `DELETE /promociones/{id}` - Eliminar promoción
- `DELETE /promociones/codigo/{codigo}` - **Eliminar promoción por código**

### 💰 Pagos (/dbd/v2/pagos)

- `POST /pagos/generar` - **Generar pago mensual con items**
- `GET /pagos` - Obtener todos los pagos
- `GET /pagos/{id}` - Obtener pago por ID
- `GET /pagos/codigo/{codigo}` - Obtener pago por código
- `GET /pagos/codigo/{codigo}/items` - Obtener pago con todos sus items (cuotas y compras)
- `GET /pagos/anio/{anio}` - Obtener pagos por año
- `PUT /pagos/codigo/{codigo}/fechas-vencimiento?primerVencimiento=YYYY-MM-DD&segundoVencimiento=YYYY-MM-DD` - **Editar fechas de vencimiento de un pago**
- `DELETE /pagos/{id}` - Eliminar pago

*Nota: Los endpoints en negrita corresponden a las funcionalidades requeridas en el trabajo final.*

## 📝 Ejemplos de Uso

### 1. Agregar una promoción de descuento a un banco

```bash
curl -X POST http://localhost:8081/dbd/v2/promociones/descuento \
  -H "Content-Type: application/json" \
  -d '{
    "codigo": "DESC-2025-001",
    "tituloPromocion": "15% de descuento",
    "nombreTienda": "Tienda XYZ",
    "cuitTienda": "30-12345678-9",
    "fechaInicioValidez": "2025-01-01",
    "fechaFinValidez": "2025-03-31",
    "comentarios": "Descuento válido para compras superiores a $50000",
    "bancoId": 1,
    "porcentajeDescuento": 15.0,
    "tope": 50000.0,
    "soloContado": false
  }'
```

### 2. Editar fechas de vencimiento de un pago

```bash
curl -X PUT "http://localhost:8081/dbd/v2/pagos/codigo/PAG-202501/fechas-vencimiento" \
  -H "Content-Type: application/json" \
  -d '{
    "primerVencimiento": "2025-01-15",
    "segundoVencimiento": "2025-01-25"
  }'
```

### 3. Generar pago mensual

```bash
curl -X POST http://localhost:8081/dbd/v2/pagos/generar \
  -H "Content-Type: application/json" \
  -d '{
    "mes": "01",
    "anio": "2025",
    "primerVencimiento": "2025-01-10",
    "segundoVencimiento": "2025-01-20",
    "recargoPrimerVencimiento": 0.0,
    "recargoSegundoVencimiento": 5.0
  }'
```

### 4. Obtener tarjetas emitidas hace más de 5 años

```bash
curl -X GET "http://localhost:8081/dbd/v2/tarjetas/emitidas?anios=5"
```

### 5. Obtener información de una compra con cuotas

```bash
curl -X GET "http://localhost:8081/dbd/v2/compras/123/detalles"
```

### 6. Eliminar una promoción por código

```bash
curl -X DELETE "http://localhost:8081/dbd/v2/promociones/codigo/DESC-2025-001"
```

### 7. Obtener promociones disponibles de un local entre fechas

```bash
curl -X GET "http://localhost:8081/dbd/v2/promociones/local/30-12345678-9?fechaInicio=2025-01-01&fechaFin=2025-03-31"
```

### 8. Obtener top 10 titulares con mayor monto en compras

```bash
curl -X GET "http://localhost:8081/dbd/v2/titulares/top-compradores?limite=10"
```

### 9. Obtener local con mayor cantidad de compras

```bash
curl -X GET "http://localhost:8081/dbd/v2/compras/local-mas-compras"
```

### 10. Obtener banco con mayor cantidad de compras

```bash
curl -X GET "http://localhost:8081/dbd/v2/bancos/mas-compras"
```

### 11. Obtener número de clientes por banco

```bash
curl -X GET "http://localhost:8081/dbd/v2/bancos/clientes-por-banco"
```

## 🎯 Funcionalidades Principales

### 1. Gestión de Compras
- ✅ Compras en **un solo pago** o en **cuotas**
- ✅ Aplicación **automática** de promociones válidas al momento de la compra
- ✅ Cálculo automático del monto final con descuentos e intereses
- ✅ Generación automática de cuotas con fechas de vencimiento
- ✅ Validación de tarjetas activas y no vencidas

### 2. Sistema de Promociones
- ✅ **Descuentos**: Porcentaje de descuento con tope opcional
  - Aplicable solo a contado o también en cuotas
  - Validación de vigencia por fechas
- ✅ **Financiaciones**: Interés especial para un número específico de cuotas
  - Reemplaza el interés estándar de la compra
- ✅ Validación automática de vigencia de promociones
- ✅ Asociación de promociones a bancos específicos

### 3. Generación de Pagos Mensuales
- ✅ Generación automática de pagos mensuales con código único
- ✅ Incluye **cuotas del mes** y **compras en un solo pago del mes anterior**
- ✅ Cálculo automático del total a pagar
- ✅ Dos fechas de vencimiento con recargos configurables
- ✅ Edición de fechas de vencimiento post-generación

### 4. Consultas Analíticas
- ✅ Top N titulares con mayor monto en compras
- ✅ Local con mayor cantidad de compras
- ✅ Banco con mayor cantidad de compras
- ✅ Número de clientes por banco
- ✅ Tarjetas emitidas hace más de N años

## 🗄️ Modelo de Datos

### Relaciones Clave

- Un **Banco** tiene muchos **Titulares**, **Tarjetas** y **Promociones**
- Un **Titular** pertenece a un **Banco** y tiene muchas **Tarjetas**
- Una **Tarjeta** pertenece a un **Titular** y un **Banco**, tiene muchas **Compras**
- Una **Compra** (abstracta) puede ser:
  - **CompraPagoUnico**: Se paga en el mes siguiente
  - **CompraCuotas**: Genera N cuotas mensuales
- Una **Promocion** (abstracta) puede ser:
  - **Descuento**: Porcentaje de descuento con tope opcional
  - **Financiacion**: Interés especial para N cuotas
- Un **Pago** agrupa **Cuotas** y **ComprasPagoUnico** de un mes específico

## 🧪 Testing

El proyecto incluye tests de integración completos que validan todos los endpoints requeridos.

**Ejecutar todos los tests**

```bash
mvn test
```

**Ejecutar solo los tests de integración**

```bash
mvn test -Dtest=ApiPagosTarjetasIntegrationTests
```

**Cobertura de Tests**

Los tests de integración `ApiPagosTarjetasIntegrationTests` validan:

1. ✅ Agregar promoción de descuento
2. ✅ Editar fechas de vencimiento de un pago
3. ✅ Generar pago mensual con items
4. ✅ Obtener tarjetas emitidas hace más de 5 años
5. ✅ Obtener información de compra con cuotas
6. ✅ Eliminar promoción por código
7. ✅ Obtener promociones disponibles por local y fechas
8. ✅ Obtener top 10 titulares con mayor monto
9. ✅ Obtener local con más compras
10. ✅ Obtener banco con más compras
11. ✅ Obtener número de clientes por banco
12. ✅ Obtener pago por código con items
13. ✅ Obtener todos los pagos
14. ✅ Crear compra en cuotas con promociones

## 🔧 Características Técnicas

**Arquitectura**

- ✅ **Arquitectura en capas**: Controller → Service → Repository
- ✅ **DTOs** para desacoplar la API de las entidades
- ✅ **Mappers** para conversión Entity ↔ DTO
- ✅ **Manejo global de excepciones** con `@RestControllerAdvice`

**Optimizaciones**

- ✅ **Carga de referencias** con `@DBRef` para optimizar queries
- ✅ **Virtual Threads** (Java 21) para mejor concurrencia
- ✅ **Connection Pooling** configurado para MongoDB

**Validaciones**

- ✅ Validación de datos con **Bean Validation** (`@Valid`, `@NotNull`, etc.)
- ✅ Validación de fechas de vencimiento
- ✅ Validación de vigencia de promociones
- ✅ Validación de tarjetas activas

## 📊 Logging

El proyecto incluye logging detallado:

- **Nivel INFO**: Operaciones principales
- **Nivel DEBUG**: Queries MongoDB y detalles de ejecución
- **Nivel TRACE**: Detalles de operaciones de base de datos

Los logs se guardan en:

- **Consola**: Formato legible para desarrollo
- **Archivo**: `logs/api-pagos-tarjetas.log` (rotación automática)

## 🚀 Despliegue

**Compilar JAR ejecutable**

```bash
mvn clean package -DskipTests
```
El JAR se genera en `target/pagos_tarjetas-1.0.0.jar`

**Ejecutar JAR**

```bash
java -jar target/pagos_tarjetas-1.0.0.jar
```

**Variables de entorno**

```bash
export SPRING_DATA_MONGODB_URI=mongodb://localhost:27017/api_pagos_tarjetas
# Con autenticación:
# export SPRING_DATA_MONGODB_URI=mongodb://username:password@host:27017/api_pagos_tarjetas
export SERVER_PORT=8081
```

## 📄 Licencia

Este proyecto fue desarrollado como trabajo final para la materia **Diseño de Bases de Datos de la Maestría en Ingeniería de Software - UNLP - 2025**.

## 👥 Autor

**Gustavo Martín Maunás**
