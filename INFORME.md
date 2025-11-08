# Informe Técnico - Decisiones de Mapeo

**Trabajo Final - DISEÑO DE BASES DE DATOS - MG IS - UNLP - 2025**

**Autor:** Gustavo Martín Maunás

---

## 1. Decisiones de Mapeo

### 1.1 Relaciones Bidireccionales

Se implementaron en:
- **Banco ↔ TitularTarjeta**
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

## 3. Tests

13 tests de integración implementados validando todas las funcionalidades requeridas.

**Archivo:** `ApiPagosTarjetasIntegrationTests.java`

## 4. Conclusión

Implementación completa con:

- ✅ Lazy loading optimizado
- ✅ Cascadas según lógica de negocio
- ✅ Transacciones para consistencia
- ✅ Herencia normalizada

**Repositorio:** https://github.com/gmmaunas/api-pagos-tarjetas
