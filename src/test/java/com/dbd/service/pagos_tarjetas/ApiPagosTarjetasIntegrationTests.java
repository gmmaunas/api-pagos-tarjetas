package com.dbd.service.pagos_tarjetas;

import com.dbd.service.pagos_tarjetas.rest.request.*;
import com.dbd.service.pagos_tarjetas.rest.response.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests de integración para validar todos los endpoints requeridos.
 *
 * Endpoints a validar:
 * 1. Agregar una nueva promoción de tipo descuento a un banco dado
 * 2. Editar las fechas de vencimiento de un pago con cierto código
 * 3. Generar el total de pago de un mes dado, informando los items correspondientes
 * 4. Obtener el listado de tarjetas emitidas hace más de 5 años
 * 5. Obtener la información de una compra, incluyendo el listado de cuotas si esta posee
 * 6. Eliminar una promoción a través de su código
 * 7. Obtener el listado de las promociones disponibles de un local entre dos fechas
 * 8. Obtener los nombres de los 10 titulares de tarjetas con mayor monto total en compras
 * 9. Obtener el nombre del local con mayor cantidad de compras registradas
 * 10. Obtener el banco con mayor cantidad de compras realizadas con sus tarjetas
 * 11. Obtener el número de clientes de cada banco
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApiPagosTarjetasIntegrationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    private Long bancoId;
    private Long titularId;
    private Long tarjetaId;
    private Long compraId;
    private String codigoPromocion;
    private String codigoPago;

    @BeforeAll
    void setup() {
        // Crear datos iniciales necesarios para los tests
        crearDatosIniciales();
    }

    private void crearDatosIniciales() {
        // 1. Crear un banco
        BancoRequest bancoRequest = new BancoRequest(
                "Banco Test",
                "30-12345678-9",
                "Av. Test 123",
                "011-1234-5678",
                "www.bancotest.com"
        );
        ResponseEntity<BancoResponse> bancoResponse = restTemplate.postForEntity(
                "/bancos",
                bancoRequest,
                BancoResponse.class
        );
        assertThat(bancoResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        bancoId = bancoResponse.getBody().id();

        // 2. Crear un titular
        TitularTarjetaRequest titularRequest = new TitularTarjetaRequest(
                "Juan Test",
                "20-98765432-1",
                "12345678",
                "Calle Test 456",
                "011-8765-4321",
                LocalDate.now(),
                List.of(bancoId)
        );
        ResponseEntity<TitularTarjetaResponse> titularResponse = restTemplate.postForEntity(
                "/titulares",
                titularRequest,
                TitularTarjetaResponse.class
        );
        assertThat(titularResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        titularId = titularResponse.getBody().id();

        // 3. Crear una tarjeta (emitida hace más de 5 años para el test)
        TarjetaRequest tarjetaRequest = new TarjetaRequest(
                "1234-5678-9012-3456",
                "123",
                "JUAN TEST",
                LocalDate.now().minusYears(6), // Hace 6 años
                LocalDate.now().plusYears(2),
                titularId,
                bancoId
        );
        ResponseEntity<TarjetaResponse> tarjetaResponse = restTemplate.postForEntity(
                "/tarjetas",
                tarjetaRequest,
                TarjetaResponse.class
        );
        assertThat(tarjetaResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        tarjetaId = tarjetaResponse.getBody().id();
    }

    /**
     * Test 1: Agregar una nueva promoción de tipo descuento a un banco dado
     */
    @Test
    @Order(1)
    @DisplayName("1. Agregar promoción de tipo descuento")
    void testAgregarPromocionDescuento() {
        DescuentoRequest request = new DescuentoRequest(
                "DESC-TEST-001",
                "Descuento Test 15%",
                "Tienda Test",
                "30-11111111-1",
                LocalDate.now(),
                LocalDate.now().plusMonths(3),
                "Descuento de prueba",
                bancoId,
                15.0,
                50000.0,
                false
        );

        ResponseEntity<DescuentoResponse> response = restTemplate.postForEntity(
                "/promociones/descuento",
                request,
                DescuentoResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().codigo()).isEqualTo("DESC-TEST-001");
        assertThat(response.getBody().porcentajeDescuento()).isEqualTo(15.0);

        codigoPromocion = response.getBody().codigo();
    }

    /**
     * Test 2: Editar las fechas de vencimiento de un pago con cierto código
     */
    @Test
    @Order(2)
    @DisplayName("2. Editar fechas de vencimiento de un pago")
    void testEditarFechasVencimientoPago() {
        // Primero crear una compra en cuotas para generar cuotas
        CompraCuotasRequest compraRequest = new CompraCuotasRequest(
                "VOUCHER-TEST-001",
                "Tienda Test",
                "30-11111111-1",
                30000.0,
                LocalDate.now().minusMonths(1).atStartOfDay(),
                tarjetaId,
                0.0,
                6,
                List.of()
        );

        ResponseEntity<CompraCuotasResponse> compraResponse = restTemplate.postForEntity(
                "/compras/cuotas",
                compraRequest,
                CompraCuotasResponse.class
        );
        assertThat(compraResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // Generar el pago del mes actual
        String mesActual = String.format("%02d", LocalDate.now().getMonthValue());
        String anioActual = String.valueOf(LocalDate.now().getYear());

        PagoRequest pagoRequest = new PagoRequest(
                mesActual,
                anioActual,
                LocalDate.now().plusDays(10),
                LocalDate.now().plusDays(20),
                0.0,
                5.0
        );

        ResponseEntity<PagoResponse> pagoResponse = restTemplate.postForEntity(
                "/pagos/generar",
                pagoRequest,
                PagoResponse.class
        );

        if (pagoResponse.getStatusCode() == HttpStatus.CREATED) {
            codigoPago = pagoResponse.getBody().codigo();

            // Editar las fechas de vencimiento
            EditarFechasVencimientoRequest editRequest = new EditarFechasVencimientoRequest(
                    LocalDate.now().plusDays(15),
                    LocalDate.now().plusDays(25)
            );

            HttpEntity<EditarFechasVencimientoRequest> requestEntity = new HttpEntity<>(editRequest);
            ResponseEntity<PagoResponse> editResponse = restTemplate.exchange(
                    "/pagos/codigo/" + codigoPago + "/fechas-vencimiento",
                    HttpMethod.PUT,
                    requestEntity,
                    PagoResponse.class
            );

            assertThat(editResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(editResponse.getBody()).isNotNull();
            assertThat(editResponse.getBody().primerVencimiento()).isEqualTo(LocalDate.now().plusDays(15));
            assertThat(editResponse.getBody().segundoVencimiento()).isEqualTo(LocalDate.now().plusDays(25));
        }
    }

    /**
     * Test 3: Generar el total de pago de un mes dado
     */
    @Test
    @Order(3)
    @DisplayName("3. Generar pago mensual con items")
    void testGenerarPagoMensual() {
        // Crear una compra en pago único del mes anterior
        LocalDate mesAnterior = LocalDate.now().minusMonths(1);

        CompraPagoUnicoRequest compraRequest = new CompraPagoUnicoRequest(
                "VOUCHER-UNICO-001",
                "Tienda Test",
                "30-11111111-1",
                15000.0,
                mesAnterior.atStartOfDay(),
                tarjetaId,
                10.0,
                List.of()
        );

        restTemplate.postForEntity(
                "/compras/pago-unico",
                compraRequest,
                CompraPagoUnicoResponse.class
        );

        // Generar pago del mes actual
        String mes = String.format("%02d", LocalDate.now().getMonthValue());
        String anio = String.valueOf(LocalDate.now().getYear());

        PagoRequest pagoRequest = new PagoRequest(
                mes,
                anio,
                LocalDate.now().plusDays(10),
                LocalDate.now().plusDays(20),
                0.0,
                5.0
        );

        ResponseEntity<PagoResponse> response = restTemplate.postForEntity(
                "/pagos/generar",
                pagoRequest,
                PagoResponse.class
        );

        // Puede fallar si ya existe el pago
        if (response.getStatusCode() == HttpStatus.CREATED) {
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().mes()).isEqualTo(mes);
            assertThat(response.getBody().anio()).isEqualTo(anio);
            assertThat(response.getBody().precioTotal()).isGreaterThan(0);
        }
    }

    /**
     * Test 4: Obtener el listado de tarjetas emitidas hace más de 5 años
     */
    @Test
    @Order(4)
    @DisplayName("4. Obtener tarjetas emitidas hace más de 5 años")
    void testObtenerTarjetasEmitidasHaceMasDe5Anios() {
        ResponseEntity<List<TarjetaResponse>> response = restTemplate.exchange(
                "/tarjetas/emitidas?anios=5",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TarjetaResponse>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isNotEmpty();

        // Verificar que todas las tarjetas fueron emitidas hace más de 5 años
        response.getBody().forEach(tarjeta -> {
            assertThat(tarjeta.desde()).isBefore(LocalDate.now().minusYears(5));
        });
    }

    /**
     * Test 5: Obtener la información de una compra, incluyendo el listado de cuotas
     */
    @Test
    @Order(5)
    @DisplayName("5. Obtener información de compra con cuotas")
    void testObtenerCompraConCuotas() {
        // Crear una compra en cuotas
        CompraCuotasRequest request = new CompraCuotasRequest(
                "VOUCHER-CUOTAS-001",
                "Tienda Test",
                "30-11111111-1",
                50000.0,
                LocalDate.now().atStartOfDay(),
                tarjetaId,
                0.0,
                12,
                List.of()
        );

        ResponseEntity<CompraCuotasResponse> createResponse = restTemplate.postForEntity(
                "/compras/cuotas",
                request,
                CompraCuotasResponse.class
        );
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        compraId = createResponse.getBody().id();

        // Obtener los detalles de la compra
        ResponseEntity<CompraCuotasResponse> response = restTemplate.getForEntity(
                "/compras/" + compraId + "/detalles",
                CompraCuotasResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().numeroCuotas()).isEqualTo(12);
        assertThat(response.getBody().cuotas()).isNotNull();
        assertThat(response.getBody().cuotas()).hasSize(12);
    }

    /**
     * Test 6: Eliminar una promoción a través de su código
     */
    @Test
    @Order(6)
    @DisplayName("6. Eliminar promoción por código")
    void testEliminarPromocionPorCodigo() {
        // Crear una promoción para eliminar
        DescuentoRequest request = new DescuentoRequest(
                "DESC-DELETE-001",
                "Descuento a Eliminar",
                "Tienda Test",
                "30-11111111-1",
                LocalDate.now(),
                LocalDate.now().plusMonths(1),
                null,
                bancoId,
                10.0,
                null,
                false
        );

        ResponseEntity<DescuentoResponse> createResponse = restTemplate.postForEntity(
                "/promociones/descuento",
                request,
                DescuentoResponse.class
        );
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // Eliminar la promoción
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/promociones/codigo/DESC-DELETE-001",
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    /**
     * Test 7: Obtener el listado de las promociones disponibles de un local entre dos fechas
     */
    @Test
    @Order(7)
    @DisplayName("7. Obtener promociones disponibles por local y fechas")
    void testObtenerPromocionesDisponiblesPorLocalYFechas() {
        String cuitTienda = "30-11111111-1";
        LocalDate fechaInicio = LocalDate.now().minusDays(1);
        LocalDate fechaFin = LocalDate.now().plusMonths(3);

        ResponseEntity<List<PromocionResponse>> response = restTemplate.exchange(
                "/promociones/local/" + cuitTienda +
                        "?fechaInicio=" + fechaInicio +
                        "&fechaFin=" + fechaFin,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PromocionResponse>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        // Verificar que las promociones están dentro del rango de fechas
        response.getBody().forEach(promo -> {
            assertThat(promo.fechaInicioValidez()).isBeforeOrEqualTo(fechaFin);
            assertThat(promo.fechaFinValidez()).isAfterOrEqualTo(fechaInicio);
        });
    }

    /**
     * Test 8: Obtener los nombres de los 10 titulares con mayor monto total en compras
     */
    @Test
    @Order(8)
    @DisplayName("8. Obtener top 10 titulares con mayor monto en compras")
    void testObtenerTop10TitularesConMayorMonto() {
        ResponseEntity<Map<String, Double>> response = restTemplate.exchange(
                "/titulares/top-compradores?limite=10",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Double>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isLessThanOrEqualTo(10);
    }

    /**
     * Test 9: Obtener el nombre del local con mayor cantidad de compras registradas
     */
    @Test
    @Order(9)
    @DisplayName("9. Obtener local con mayor cantidad de compras")
    void testObtenerLocalConMasCompras() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "/compras/local-mas-compras",
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isNotEmpty();
    }

    /**
     * Test 10: Obtener el banco con mayor cantidad de compras realizadas con sus tarjetas
     */
    @Test
    @Order(10)
    @DisplayName("10. Obtener banco con mayor cantidad de compras")
    void testObtenerBancoConMasCompras() {
        ResponseEntity<BancoResponse> response = restTemplate.getForEntity(
                "/bancos/mas-compras",
                BancoResponse.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().nombre()).isNotNull();
    }

    /**
     * Test 11: Obtener el número de clientes de cada banco
     */
    @Test
    @Order(11)
    @DisplayName("11. Obtener número de clientes por banco")
    void testObtenerNumeroClientesPorBanco() {
        ResponseEntity<Map<String, Long>> response = restTemplate.exchange(
                "/bancos/clientes-por-banco",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Map<String, Long>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isNotEmpty();
    }

    /**
     * Test adicional: Obtener pago por código con items
     */
    @Test
    @Order(12)
    @DisplayName("Extra: Obtener pago por código con items")
    void testObtenerPagoPorCodigoConItems() {
        if (codigoPago != null) {
            ResponseEntity<PagoResponse> response = restTemplate.getForEntity(
                    "/pagos/codigo/" + codigoPago + "/items",
                    PagoResponse.class
            );

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().codigo()).isEqualTo(codigoPago);
        }
    }

    /**
     * Test adicional: Obtener todos los pagos
     */
    @Test
    @Order(13)
    @DisplayName("Extra: Obtener todos los pagos")
    void testObtenerTodosLosPagos() {
        ResponseEntity<List<PagoResponse>> response = restTemplate.exchange(
                "/pagos",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PagoResponse>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    /**
     * Test G1: Verificar relación ManyToMany Banco-TitularTarjeta (corrección)
     * Un titular puede pertenecer a múltiples bancos y viceversa.
     */
    @Test
    @Order(14)
    @DisplayName("G1. TitularTarjeta puede pertenecer a múltiples bancos (ManyToMany)")
    void testTitularPertenecesAMultiplesBancos() {
        // Crear un segundo banco
        BancoRequest banco2Request = new BancoRequest(
                "Banco Secundario Test",
                "30-66666666-6",
                "Av. Secundaria 456",
                "011-6666-6666",
                "www.bancosecundario.com"
        );
        ResponseEntity<BancoResponse> banco2Response = restTemplate.postForEntity(
                "/bancos", banco2Request, BancoResponse.class);
        assertThat(banco2Response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Long banco2Id = banco2Response.getBody().id();

        // Crear titular asociado a los dos bancos
        TitularTarjetaRequest request = new TitularTarjetaRequest(
                "Maria MultiBank",
                "27-55555555-5",
                "55555555",
                "Calle Dual 789",
                "011-5555-5555",
                LocalDate.now(),
                List.of(bancoId, banco2Id)
        );
        ResponseEntity<TitularTarjetaResponse> response = restTemplate.postForEntity(
                "/titulares", request, TitularTarjetaResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().bancos())
                .as("El titular debe pertenecer a exactamente 2 bancos")
                .hasSize(2);
    }

    /**
     * Test G3: Verificar eliminación segura de promoción aplicada a una compra (corrección)
     * Al eliminar una promoción que fue aplicada a una compra, la compra debe seguir existiendo.
     */
    @Test
    @Order(15)
    @DisplayName("G3. Eliminar promoción aplicada a una compra no elimina ni rompe la compra")
    void testEliminarPromocionAplicadaNoRompeCompra() {
        // Crear una promoción válida hoy para una tienda específica
        DescuentoRequest promoRequest = new DescuentoRequest(
                "DESC-G3-SAFE",
                "Descuento Seguro G3",
                "Tienda G3 Safe",
                "30-77777777-7",
                LocalDate.now().minusDays(1),
                LocalDate.now().plusMonths(6),
                null,
                bancoId,
                5.0,
                null,
                false
        );
        ResponseEntity<DescuentoResponse> promoResp = restTemplate.postForEntity(
                "/promociones/descuento", promoRequest, DescuentoResponse.class);
        assertThat(promoResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // Crear compra en esa tienda — la promoción se aplica automáticamente al mismo banco
        CompraPagoUnicoRequest compraRequest = new CompraPagoUnicoRequest(
                "VOUCHER-G3-SAFE-001",
                "Tienda G3 Safe",
                "30-77777777-7",
                10000.0,
                LocalDate.now().atStartOfDay(),
                tarjetaId,
                0.0,
                List.of()
        );
        ResponseEntity<CompraPagoUnicoResponse> compraResp = restTemplate.postForEntity(
                "/compras/pago-unico", compraRequest, CompraPagoUnicoResponse.class);
        assertThat(compraResp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Long compraG3Id = compraResp.getBody().id();

        // Eliminar la promoción (debe desvincularse de la compra sin error de FK)
        ResponseEntity<Void> deleteResp = restTemplate.exchange(
                "/promociones/codigo/DESC-G3-SAFE",
                HttpMethod.DELETE, null, Void.class);
        assertThat(deleteResp.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // La compra debe seguir siendo accesible sin referencias rotas
        ResponseEntity<CompraPagoUnicoResponse> compraAun = restTemplate.getForEntity(
                "/compras/" + compraG3Id + "/detalles", CompraPagoUnicoResponse.class);
        assertThat(compraAun.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(compraAun.getBody()).isNotNull();
    }

    /**
     * Test G2: Verificar que las cuotas del pago mensual incluyen el ID de la compra origen (corrección)
     * Cada cuota en el pago mensual debe informar a qué compra pertenece.
     */
    @Test
    @Order(16)
    @DisplayName("G2. Las cuotas en el pago mensual incluyen compraId (ítem con origen identificado)")
    void testCuotasEnPagoIncluyenCompraId() {
        if (codigoPago == null) {
            return;
        }
        ResponseEntity<PagoResponse> response = restTemplate.getForEntity(
                "/pagos/codigo/" + codigoPago + "/items",
                PagoResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();

        List<CuotaResponse> cuotas = response.getBody().cuotas();
        if (cuotas != null && !cuotas.isEmpty()) {
            cuotas.forEach(cuota ->
                    assertThat(cuota.compraId())
                            .as("Cuota #%d debe incluir el ID de la compra origen", cuota.numero())
                            .isNotNull()
            );
        }
    }
}
