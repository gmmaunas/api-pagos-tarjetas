# Informe Técnico - Decisiones de Mapeo

**Trabajo Final - DISEÑO DE BASES DE DATOS - MG IS - UNLP - 2025**

**Autor:** Gustavo Martín Maunás

---

## 1. Decisiones de Mapeo

### 1.1 Relaciones Bidireccionales

Se implementaron en:
- **Banco ↔ TitularTarjeta** — relación **ManyToMany** con tabla intermedia `banco_titular` (`titular_id`, `banco_id`). Un titular puede pertenecer a múltiples bancos y un banco puede tener múltiples titulares, respetando el diagrama del enunciado.
- **Pago ↔ Cuota**
- **Tarjeta ↔ Compra**

**Justificación:** Facilitan la navegación entre entidades y permiten operaciones en cascada.

---

### 1.2 Carga Lazy (Bajo Demanda)

Todas las relaciones usan `FetchType.LAZY` por defecto para optimizar el rendimiento.

**Estrategia de carga:**
Se implementaron queries personalizadas con `JOIN FETCH` para cargar explícitamente las asociaciones necesarias. Cuando se requieren múltiples colecciones, se utilizan queries especializadas separadas.

**Ejemplo:**
```java
@Query("SELECT p FROM Pago p LEFT JOIN FETCH p.cuotas WHERE p.codigo = :codigo")
Optional<Pago> findByCodigoConCuotas(@Param("codigo") String codigo);
```

### 1.3 Operaciones en Cascada

- **Pago → Cuotas:** `CascadeType.ALL` (las cuotas no existen sin un pago)
- **Compra → Promociones:** `CascadeType.PERSIST`, `MERGE` (las promociones son independientes)
- **Tarjeta → Compras:** Sin cascada (mantener historial de compras)

### 1.4 Embeber Objetos vs Referencias

Se utilizaron referencias (relaciones JPA) en lugar de @Embedded.

**Justificación:** Todas las entidades tienen identidad propia y ciclo de vida independiente. Esto mantiene la normalización y permite reutilización.

### 1.5 Transacciones

Se usa `@Transactional` en la capa de servicio para garantizar atomicidad en operaciones complejas como la generación de pagos mensuales.

**Justificación:** Garantiza consistencia y permite rollback automático en caso de error.

### 1.6 Herencia

Estrategia `JOINED` para:

- **Compra** → `CompraPagoUnico`, `CompraCuotas`
- **Promocion** → `Descuento`, `Financiacion`

**Justificación:** Normalización completa sin columnas NULL innecesarias.

## 2. Configuración y Ejecución

**Base de datos:** MySQL 8.0+ (se crea automáticamente al iniciar)

**Ejecutar:**
```bash
mvn clean install
mvn spring-boot:run
```

**Tests:**
```bash
mvn test
```

**API:** http://localhost:8080/dbd/v1/swagger-ui.html

**Detalles completos:** Ver `README.md`

## 3. Correcciones Aplicadas

### 3.1 Relación Banco ↔ TitularTarjeta (ManyToMany)

**Problema:** La relación estaba implementada como `OneToMany` (un banco, muchos titulares), sin tabla intermedia, lo que no respetaba el diagrama donde un titular puede pertenecer a múltiples bancos.

**Solución:** Se cambió a `@ManyToMany` con tabla `banco_titular`. `TitularTarjetaRequest` ahora acepta `List<Long> bancoIds`. `TitularTarjetaResponse` devuelve `List<BancoResponse> bancos`.

### 3.2 Generación de pago mensual — ítems correctamente informados

**Problema 1:** `CuotaResponse` no incluía el ID de la compra origen (`compraId`), imposibilitando identificar a qué compra pertenece cada cuota dentro de un pago.

**Solución:** Se agregó el campo `compraId` (columna `compra_id` ya existente en la tabla `cuotas`) a `Cuota.java` y a `CuotaResponse`, y se actualizó `CompraMapper.toCuotaResponse`.

**Problema 2:** `Descuento.aplicarAPagoUnico()` tenía la condición invertida: `if (!soloContado) return 0.0`, por lo que descuentos con `soloContado=false` **no se aplicaban** a compras al contado, produciendo un `montoFinal` incorrecto y, en consecuencia, un `precioTotal` incorrecto en el pago.

**Solución:** Se eliminó la condición invertida. Un `Descuento` siempre se aplica a pagos al contado; `soloContado` solo restringe su aplicación a compras en cuotas.

### 3.3 Eliminación de promoción con compras asociadas

**Problema:** Al eliminar una promoción con `DELETE`, si existían registros en la tabla `compra_promocion` (relación ManyToMany con `Compra`), la base de datos lanzaba una violación de clave foránea, o los datos quedaban inconsistentes.

**Solución:** Antes del `delete`, se itera sobre las compras asociadas (`promocion.getCompras()`) y se llama a `compra.getPromocionesAplicadas().remove(promocion)` desde el lado propietario de la relación. Hibernate elimina la fila de `compra_promocion` dentro de la misma transacción, permitiendo luego el `DELETE` de la promoción sin errores.

## 4. Tests

13 tests de integración implementados validando todas las funcionalidades requeridas.

**Archivo:** `ApiPagosTarjetasIntegrationTests.java`

## 5. Conclusión

Implementación completa con:

- ✅ Lazy loading optimizado
- ✅ Cascadas según lógica de negocio
- ✅ Transacciones para consistencia
- ✅ Herencia normalizada
- ✅ Modelo de datos corregido (ManyToMany Banco ↔ TitularTarjeta)
- ✅ Pago mensual con ítems correctamente informados
- ✅ Eliminación segura de promociones

**Repositorio:** https://github.com/gmmaunas/api-pagos-tarjetas
