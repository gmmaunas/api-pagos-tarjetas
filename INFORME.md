# Informe Técnico - Decisiones de Mapeo

**Trabajo Final - DISEÑO DE BASES DE DATOS - MG IS - UNLP - 2025**

**Autor:** Gustavo Martín Maunás

---

## 1. Decisiones de Mapeo para MongoDB

### 1.1 Relaciones: Referencias vs Embebido

**Referencias (@DBRef):**
- **Banco ↔ TitularTarjeta:** Referencia (normalización)
- **TitularTarjeta → Banco:** Referencia unidireccional
- **Tarjeta → TitularTarjeta, Banco:** Referencias
- **Compra → Tarjeta, Promociones:** Referencias
- **Promocion → Banco:** Referencia

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
- **Integridad histórica:** Las compras mantienen referencias a promociones eliminadas (datos históricos)

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
- `Banco.cuit` - Garantiza unicidad de CUIT por banco
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

## 3. Tests

14 tests de integración implementados validando todas las funcionalidades requeridas.

**Tecnología de testing:** MongoDB embebido (Flapdoodle) para independencia del entorno.

**Archivo:** `ApiPagosTarjetasIntegrationTests.java`

## 4. Conclusión

Implementación completa con:

- ✅ Lazy loading optimizado con `@DBRef`
- ✅ Cascadas manuales según lógica de negocio
- ✅ Operaciones atómicas a nivel documento
- ✅ Herencia con discriminador de tipo
- ✅ Índices únicos para integridad de datos
- ✅ Embebido vs Referencias según patrón de acceso

**Repositorio:** https://github.com/gmmaunas/api-pagos-tarjetas
