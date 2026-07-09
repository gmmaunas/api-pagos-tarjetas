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
- **Compra → Promocion:** Sin cascada — `promocionAplicada` es una referencia `@ManyToOne` (0..1), gestionada por el servicio
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

**Problema:** Al eliminar una promoción con `DELETE`, si existían compras que la referenciaban como `promocionAplicada`, la base de datos lanzaba una violación de clave foránea (`promocion_id` FK en tabla `compras`).

**Solución:** Antes del `delete`, se itera sobre `promocion.getCompras()` y se llama `compra.setPromocionAplicada(null)`. Hibernate actualiza la FK en `compras` dentro de la misma transacción, permitiendo luego el `DELETE` de la promoción sin errores.

### 3.4 Cálculo de compra en cuotas con descuento (Corrección 1)

**Problema:** En `CompraCuotas.calcularMontoFinal()`, al haber un `Descuento` como promoción, el porcentaje de descuento se usaba como tasa de interés. Ej.: un descuento del 20% producía `montoFinal = monto * (1 + 20/100)` en lugar de restar el descuento.

**Solución:** Se delegó el cálculo a métodos polimórficos de `Promocion`, sin `instanceof`:
- `calcularInteresParaCuotas(compra)`: `Financiacion` devuelve su tasa de reemplazo, `Descuento` devuelve `null`
- `calcularDescuentoParaCuotas(compra)`: `Descuento` devuelve el importe a restar, `Financiacion` devuelve `0.0`

```java
Double interesPromocion = promo.calcularInteresParaCuotas(this);
if (interesPromocion != null) interesAplicado = interesPromocion;
descuentoTotal = promo.calcularDescuentoParaCuotas(this);
setMontoFinal(getMonto() * (1 + interesAplicado / 100) - descuentoTotal);
```

### 3.5 Modelo 0..1 Compra ↔ Promoción (Corrección 2 + Corrección 3)

**Problema:** El diagrama de clases especifica que una `Compra` puede tener 0 o 1 `Promoción`, pero la implementación usaba `@ManyToMany List<Promocion> promocionesAplicadas`, permitiendo acumulación ilimitada.

**Solución:**
- `Compra.java`: reemplazado por `@ManyToOne Promocion promocionAplicada` (FK `promocion_id` en tabla `compras`)
- `Promocion.java`: relación inversa cambiada a `@OneToMany(mappedBy="promocionAplicada")`
- `CompraServiceImpl`: métodos `crearCompraPagoUnico()` y `crearCompraCuotas()` eligen automáticamente la única promoción válida por banco + tienda + fecha
- Responses: `promocionesAplicadas: List<>` reemplazado por `promocionAplicada: PromocionResponse` (campo único, puede ser null)

### 3.6 Cálculo duplicado y lazy loading (Corrección 4)

**Problema 1 — Cálculo duplicado:** `CompraMapper.toEntity()` llamaba a `calcularMontoFinal()` y `generarCuotas()`, pero `CompraServiceImpl` también los llamaba. Cada compra se calculaba dos veces.

**Solución:** Se eliminó el cálculo del mapper. Únicamente el servicio realiza el cálculo.

**Problema 2 — Lazy loading fuera de transacción:** `obtenerTodasLasCompras()` usaba `findAll()`, lo que causaba `LazyInitializationException` al serializar `tarjeta` y `promocionAplicada` fuera de la sesión de Hibernate.

**Solución:** Se agregaron queries JPQL con `JOIN FETCH`:
```java
@Query("SELECT DISTINCT c FROM Compra c LEFT JOIN FETCH c.tarjeta LEFT JOIN FETCH c.promocionAplicada vp LEFT JOIN FETCH vp.banco")
List<Compra> findAllConDetalles();
```

## 4. Tests

**20 tests** implementados (19 integración + 1 application) — todos pasando.

**Archivo:** `ApiPagosTarjetasIntegrationTests.java`

| # | Descripción | Verifica |
|---|---|---|
| 1-11 | Requerimientos funcionales del enunciado | Endpoints requeridos |
| 12-13 | Extra: pagos y listados | Flujo completo |
| G1 | TitularTarjeta pertenece a múltiples bancos | ManyToMany correcto |
| G3 | Eliminar promoción no rompe compras | FK sin violación |
| G2 | Cuotas en pago incluyen `compraId` | Identificación de origen |
| Corrección 1 | Descuento en cuotas = importe deducido | Cálculo correcto |
| Corrección 2 | Compra tiene UNA promoción | Modelo 0..1 |
| Corrección 3 | Listar compras sin LazyInitializationException | JOIN FETCH |

## 5. Conclusión

Implementación completa con todas las correcciones de revisión aplicadas:

- ✅ Lazy loading optimizado con `JOIN FETCH`
- ✅ Cascadas según lógica de negocio
- ✅ Transacciones para consistencia
- ✅ Herencia normalizada (JOINED strategy)
- ✅ ManyToMany Banco ↔ TitularTarjeta correctamente implementado
- ✅ Pago mensual con ítems correctamente informados (`compraId` en cuotas)
- ✅ Eliminación segura de promociones (`setPromocionAplicada(null)` antes del DELETE)
- ✅ Modelo 0..1 Compra ↔ Promoción (`@ManyToOne promocionAplicada`)
- ✅ Diseño OOP sin `instanceof`: polimorfismo puro en `Promocion` con `calcularInteresParaCuotas`, `calcularDescuentoParaCuotas` y predicados `seAplica*()`
- ✅ Cálculo correcto de montos: Descuento resta, Financiación reemplaza tasa
- ✅ Sin cálculo duplicado: solo el servicio calcula `montoFinal`

**Repositorio:** https://github.com/gmmaunas/api-pagos-tarjetas
