package com.dbd.service.pagos_tarjetas.service.impl;

import com.dbd.service.pagos_tarjetas.model.CompraCuotas;
import com.dbd.service.pagos_tarjetas.model.CompraPagoUnico;
import com.dbd.service.pagos_tarjetas.model.Cuota;
import com.dbd.service.pagos_tarjetas.model.Pago;
import com.dbd.service.pagos_tarjetas.repository.CompraCuotasRepository;
import com.dbd.service.pagos_tarjetas.repository.CompraPagoUnicoRepository;
import com.dbd.service.pagos_tarjetas.repository.PagoRepository;
import com.dbd.service.pagos_tarjetas.service.IPagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagoServiceImpl implements IPagoService {

    private final PagoRepository pagoRepository;
    private final CompraPagoUnicoRepository compraPagoUnicoRepository;
    private final CompraCuotasRepository compraCuotasRepository;
    private final MongoTemplate mongoTemplate;

    // Generar el total de pago de un mes dado, informando los items correspondientes
    @Override
    public Pago generarPagoMensual(String mes, String anio, LocalDate primerVencimiento,
                                   LocalDate segundoVencimiento, Double recargoPrimero,
                                   Double recargoSegundo) {

        // Verificar si ya existe un pago para ese mes y año
        pagoRepository.findByMesAndAnio(mes, anio).ifPresent(p -> {
            throw new IllegalArgumentException("Ya existe un pago para el mes " + mes + "/" + anio);
        });

        // Obtener compras en cuotas del mes para extraer cuotas
        Query queryCuotas = new Query();
        queryCuotas.addCriteria(Criteria.where("cuotas.mes").is(mes)
                .and("cuotas.anio").is(anio)
                .and("cuotas.pagoId").exists(false));

        List<CompraCuotas> comprasCuotas = mongoTemplate.find(queryCuotas, CompraCuotas.class);
        List<Cuota> cuotasDelMes = new ArrayList<>();

        for (CompraCuotas compra : comprasCuotas) {
            for (Cuota cuota : compra.getCuotas()) {
                if (cuota.getMes().equals(mes) && cuota.getAnio().equals(anio) && cuota.getPagoId() == null) {
                    cuota.setCompraId(compra.getId());
                    cuotasDelMes.add(cuota);
                }
            }
        }

        // Obtener compras en un solo pago del mes anterior sin asignar
        int mesInt = Integer.parseInt(mes);
        int anioInt = Integer.parseInt(anio);
        int mesAnterior = mesInt == 1 ? 12 : mesInt - 1;
        int anioAnterior = mesInt == 1 ? anioInt - 1 : anioInt;

        LocalDateTime inicioMes = LocalDateTime.of(anioAnterior, mesAnterior, 1, 0, 0);
        LocalDateTime finMes = inicioMes.plusMonths(1).minusSeconds(1);

        Query queryCompras = new Query();
        queryCompras.addCriteria(Criteria.where("fechaHora").gte(inicioMes).lte(finMes)
                .and("pago").is(null));

        List<CompraPagoUnico> comprasPagoUnico = mongoTemplate.find(queryCompras, CompraPagoUnico.class);

        // Crear el pago
        Pago pago = Pago.builder()
                .codigo("PAG-" + anio + mes)
                .mes(mes)
                .anio(anio)
                .primerVencimiento(primerVencimiento)
                .segundoVencimiento(segundoVencimiento)
                .recargoPrimerVencimiento(recargoPrimero)
                .recargoSegundoVencimiento(recargoSegundo)
                .precioTotal(0.0)
                .cuotas(new ArrayList<>(cuotasDelMes))
                .comprasPagoUnicoIds(new ArrayList<>())
                .build();

        // Guardar el pago primero para obtener su ID
        pago = pagoRepository.save(pago);

        // Asignar cuotas al pago (actualizar pagoId en las cuotas embebidas)
        for (CompraCuotas compra : comprasCuotas) {
            boolean actualizado = false;
            for (Cuota cuota : compra.getCuotas()) {
                if (cuota.getMes().equals(mes) && cuota.getAnio().equals(anio) && cuota.getPagoId() == null) {
                    cuota.setPagoId(pago.getId());
                    actualizado = true;
                }
            }
            if (actualizado) {
                compraCuotasRepository.save(compra);
            }
        }

        // Asignar compras en un solo pago
        for (CompraPagoUnico compra : comprasPagoUnico) {
            compra.setPago(pago);
            pago.getComprasPagoUnicoIds().add(compra.getId());
            compraPagoUnicoRepository.save(compra);
        }

        // Calcular precio total (solo cuotas, las compras se suman en el servicio)
        Double totalCuotas = cuotasDelMes.stream().mapToDouble(Cuota::getPrecio).sum();
        Double totalCompras = comprasPagoUnico.stream().mapToDouble(CompraPagoUnico::getMontoFinal).sum();
        pago.setPrecioTotal(totalCuotas + totalCompras);

        return pagoRepository.save(pago);
    }

    // Editar las fechas de vencimiento de un pago con cierto código
    @Override
    public Pago editarFechasVencimiento(String codigo, LocalDate nuevoPrimerVencimiento,
                                        LocalDate nuevoSegundoVencimiento) {
        Pago pago = pagoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con código: " + codigo));

        pago.setPrimerVencimiento(nuevoPrimerVencimiento);
        pago.setSegundoVencimiento(nuevoSegundoVencimiento);

        return pagoRepository.save(pago);
    }

    @Override
    public Pago obtenerPagoPorCodigoConItems(String codigo) {
        Pago pago = pagoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con código: " + codigo));

        // Cargar compras de pago único
        if (pago.getComprasPagoUnicoIds() != null && !pago.getComprasPagoUnicoIds().isEmpty()) {
            List<CompraPagoUnico> compras = compraPagoUnicoRepository.findAllById(pago.getComprasPagoUnicoIds());
            pago.setComprasPagoUnico(compras);
        }

        return pago;
    }

    @Override
    public Pago obtenerPagoPorId(String id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con id: " + id));

        // Cargar compras de pago único
        if (pago.getComprasPagoUnicoIds() != null && !pago.getComprasPagoUnicoIds().isEmpty()) {
            List<CompraPagoUnico> compras = compraPagoUnicoRepository.findAllById(pago.getComprasPagoUnicoIds());
            pago.setComprasPagoUnico(compras);
        }

        return pago;
    }

    @Override
    public Pago obtenerPagoPorCodigo(String codigo) {
        Pago pago = pagoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con código: " + codigo));

        // Cargar compras de pago único
        if (pago.getComprasPagoUnicoIds() != null && !pago.getComprasPagoUnicoIds().isEmpty()) {
            List<CompraPagoUnico> compras = compraPagoUnicoRepository.findAllById(pago.getComprasPagoUnicoIds());
            pago.setComprasPagoUnico(compras);
        }

        return pago;
    }

    @Override
    public List<Pago> obtenerTodosLosPagos() {
        List<Pago> pagos = pagoRepository.findAll();

        // Cargar compras para cada pago
        for (Pago pago : pagos) {
            if (pago.getComprasPagoUnicoIds() != null && !pago.getComprasPagoUnicoIds().isEmpty()) {
                List<CompraPagoUnico> compras = compraPagoUnicoRepository.findAllById(pago.getComprasPagoUnicoIds());
                pago.setComprasPagoUnico(compras);
            }
        }

        return pagos;
    }

    @Override
    public List<Pago> obtenerPagosPorAnio(String anio) {
        return pagoRepository.findByAnio(anio);
    }

    @Override
    public void eliminarPago(String id) {
        pagoRepository.deleteById(id);
    }
}
