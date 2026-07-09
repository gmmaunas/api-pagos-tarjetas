# Informe Técnico - Decisiones de Mapeo

**Trabajo Final - DISEÑO DE BASES DE DATOS - MG IS - UNLP - 2025**

**Autor:** Gustavo Martín Maunás

---

## 1. Decisiones de Mapeo para MongoDB

### 1.1 Relaciones: Referencias vs Embebido

**Referencias (@DBRef):**
- **Banco ↔ TitularTarjeta:** Relación ManyToMany embebida (`TitularTarjeta` almacena `List<Banco>` embebido, navegación inversa vía servicio)
- **Tarjeta → TitularTarjeta, Banco:** Referencias
- **Compra → Tarjeta, Promocion:** `@DBRef` a una única `promocionAplicada` (0..1, nunca una lista)
- **Promocion → Banco:** Embebido

**Embebido:**
- **CompraCuotas → Cuotas:** Embebido (las cuotas no existen sin compra)
- **Pago → Cuotas:** Embebido (snapshot del estado al generar pago)

**Justificación:**
- Se usan referencias para entidades independientes que se reutilizan
- Se embeben datos que se consultan juntos y no cambian

---

### 1.2 Carga Lazy (Bajo Demanda)

MongoDB con Spring Data carga las referencias `@DBRef` de forma lazy por defecto.

**Estrategia:**
- Referencias se cargan solo cuando se accede a ellas
- Documentos embebidos se cargan siempre con el documento padre
- Optimización mediante proyecciones cuando solo se necesitan campos específicos

---

### 1.3 Operaciones en Cascada

MongoDB NO soporta cascadas automáticas como JPA.

**Estrategia implementada:**
- **Eliminación manual:** Al eliminar una entidad padre, se debe decidir explícitamente qué hacer con las referencias
- **Desvinculación activa:** Al eliminar una promoción, se recorren todas las compras que la referencian y se elimina la referencia antes del delete, evitando DBRefs huérfanos

---

### 1.4 Embeber vs Referencias

**Criterios de decisión:**

| Relación | Decisión | Justificación |
|---|---|---|
| Compra → Cuotas | Embebido | Cuotas no existen sin compra, se consultan juntas |
| Pago → Cuotas | Embebido | Snapshot inmutable del estado |
| Compra → Tarjeta | Referencia | Tarjeta es entidad independiente |
| Compra → Promociones | Referencia | Promociones se reutilizan |
| Pago → Compras Pago Único | Lista de IDs | Evita duplicar documentos grandes |

---

### 1.5 Transacciones

**Limitación:** MongoDB embebido (Flapdoodle) no soporta transacciones multi-documento.

**Estrategia:**
- Operaciones atómicas a nivel documento
- Para producción con replica sets, usar `@Transactional`
- Diseño de documentos que minimiza necesidad de transacciones

---

### 1.6 Herencia

**Estrategia:** Discriminador implícito por tipo de clase

- **Compra** → `CompraPagoUnico`, `CompraCuotas`
- **Promocion** → `Descuento`, `Financiacion`

**Implementación:**
- Todas las subclases se almacenan en la misma colección
- Campo `tipo` identifica el tipo de documento
- Método abstracto `getTipo()` implementado en cada subclase

**Justificación:** MongoDB no tiene estrategias de herencia como JPA (SINGLE_TABLE, JOINED, TABLE_PER_CLASS). Todos los documentos se almacenan en una colección con un discriminador.

---

### 1.7 Índices

**Índices únicos implementados:**
- `Banco.cuit` - Validación programática en servicio (el `@Indexed(unique=true)` fue eliminado para evitar `DuplicateKeyException` en documentos embebidos)
- `TitularTarjeta.cuit` - Garantiza unicidad de CUIT por titular
- `Tarjeta.numero` - Garantiza unicidad de número de tarjeta
- `Promocion.codigo` - Garantiza unicidad de código de promoción
- `Pago.codigo` - Garantiza unicidad de código de pago

**Configuración:** `auto-index-creation: true` en `application.yml`

**Justificación:**
- Mejora el rendimiento de búsquedas por campos únicos
- Garantiza integridad de datos a nivel de base de datos
- MongoDB crea índices automáticamente al iniciar la aplicación

---

## 2. Configuración y Ejecución

**Base de datos:** MongoDB 7.0+ (la base de datos y colecciones se crean automáticamente)

**Ejecutar:**
```bash
mvn clean install
mvn spring-boot:run
```

**Tests:**
```bash
mvn test
```

**API:** http://localhost:8081/dbd/v2/swagger-ui.html

**Detalles completos:** Ver `README.md`

## 3. Correcciones Aplicadas

### Corrección 1 — Cálculo de descuento en cuotas

`CompraCuotas.calcularMontoFinal()` usaba `aplicarACuotas()` de forma ambigua: el valor devuelto por `Descuento` (importe a descontar) era tratado como tasa de interés, elevando el monto en lugar de reducirlo.

**Fix:** Se agregaron métodos polimórficos `calcularInteresParaCuotas()` y `calcularDescuentoParaCuotas()` en la clase abstracta `Promocion`. `Financiacion` implementa el primero y `Descuento` el segundo. `calcularMontoFinal()` invoca ambos sin instanceof, manteniendo el diseño OOP.

### Corrección 2 — Acumulación de promociones (modelo 0..1)

`Compra` tenía `List<Promocion> promocionesAplicadas`, permitiendo asociar múltiples promociones a una sola compra, violando la cardinalidad 0..1 requerida.

**Fix:** Se reemplazó la lista por un campo único `Promocion promocionAplicada` (`@DBRef`). `CompraServiceImpl.seleccionarPromocionParaCompra()` elige la **primera** promoción válida por banco/tienda/fecha. Los responses (`CompraResponse`, etc.) exponen `promocionAplicada` como objeto único (no lista).

### Corrección 3 — Bidireccionalidad Banco-Titular

La relación Banco↔TitularTarjeta era unidireccional (solo TitularTarjeta conoce sus bancos). No existía forma de obtener los titulares de un banco.

**Fix:** Se implementó `GET /bancos/{id}/titulares` con lógica Java en `BancoServiceImpl.obtenerTitularesPorBanco()`, filtrando titulares cuya lista `bancos` contenga el banco buscado.

### Corrección 4 — PUT /promociones/{id} devolvía 400

El endpoint `PUT /promociones/{id}` retornaba hardcodeado un `400 Bad Request`.

**Fix:** Se creó `PromocionUpdateRequest` (tituloPromocion, fechas, comentarios) y el controlador ahora delega a `PromocionServiceImpl.actualizarPromocion()`, retornando el response polimórfico correcto.

### Corrección 5 — Datos colgantes al eliminar Pago o Banco

Eliminar un `Pago` dejaba `pagoId` en cuotas y `pago` en `CompraPagoUnico` apuntando a un documento inexistente. Eliminar un `Banco` dejaba referencias embebidas huérfanas en `Tarjeta`, `Promocion` y `TitularTarjeta`.

**Fix:** `PagoServiceImpl.eliminarPago()` limpia `pagoId` en cuotas y `pago` en compras antes de borrar. `BancoServiceImpl.eliminarBanco()` limpia el banco embebido en Tarjeta/Promocion y lo quita de las listas en TitularTarjeta.

### Corrección 6 — Filtro mensual de cuotas con $elemMatch

La query de `PagoServiceImpl.generarPagoMensual()` filtraba con condiciones separadas sobre el array `cuotas`, lo que permitía que diferentes cuotas satisfagan cada condición (cross-element matching de MongoDB).

**Fix:** Se reemplazó por `Criteria.where("cuotas").elemMatch(...)`, garantizando que **las tres condiciones** (mes, año, pagoId==null) se evalúen sobre la **misma cuota**.

## 4. Tests

22 tests de integración implementados (16 originales + 6 correcciones + 1 application context).

**Tecnología de testing:** MongoDB embebido (Flapdoodle) para independencia del entorno.

**Archivo:** `ApiPagosTarjetasIntegrationTests.java`

## 5. Conclusión

Implementación completa con:

- ✅ Lazy loading optimizado con `@DBRef`
- ✅ Cascadas manuales según lógica de negocio
- ✅ Operaciones atómicas a nivel documento
- ✅ Herencia con discriminador de tipo
- ✅ Validación programática de unicidad de CUIT de banco
- ✅ Embebido vs Referencias según patrón de acceso
- ✅ Modelo 0..1 para `promocionAplicada` en `Compra`
- ✅ Cálculo polimórfico sin instanceof para descuento/financiación en cuotas
- ✅ Eliminación segura de entidades sin referencias colgantes
- ✅ Bidireccionalidad Banco↔TitularTarjeta navegable via API

**Repositorio:** https://github.com/gmmaunas/api-pagos-tarjetas
