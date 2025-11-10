package com.dbd.service.pagos_tarjetas.service.impl;

import com.dbd.service.pagos_tarjetas.model.*;
import com.dbd.service.pagos_tarjetas.repository.*;
import com.dbd.service.pagos_tarjetas.service.ICompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
@RequiredArgsConstructor
public class CompraServiceImpl implements ICompraService {

    private final CompraRepository compraRepository;
    private final CompraPagoUnicoRepository compraPagoUnicoRepository;
    private final CompraCuotasRepository compraCuotasRepository;
    private final PromocionRepository promocionRepository;
    private final TarjetaRepository tarjetaRepository;
    private final MongoTemplate mongoTemplate;

    // Crear compra en un solo pago
    @Override
    public CompraPagoUnico crearCompraPagoUnico(CompraPagoUnico compra) {
        // Aplicar promociones válidas
        aplicarPromocionesACompra(compra);

        // Calcular monto final
        compra.calcularMontoFinal();

        return compraPagoUnicoRepository.save(compra);
    }

    // Crear compra en cuotas
    @Override
    public CompraCuotas crearCompraCuotas(CompraCuotas compra) {
        // Aplicar promociones válidas
        aplicarPromocionesACompra(compra);

        // Calcular monto final
        compra.calcularMontoFinal();

        // Generar cuotas
        compra.generarCuotas();

        return compraCuotasRepository.save(compra);
    }

    // Aplicar promociones válidas a una compra
    private void aplicarPromocionesACompra(Compra compra) {
        Tarjeta tarjeta = tarjetaRepository.findById(compra.getTarjeta().getId())
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));

        String bancoId = tarjeta.getBanco().getId();
        String cuitTienda = compra.getCuitTienda();
        LocalDate fechaCompra = compra.getFechaHora().toLocalDate();

        // Obtener promociones válidas del banco para la tienda y fecha
        List<Promocion> promocionesValidas = promocionRepository.findAll()
                .stream()
                .filter(p -> p.getBanco().getId().equals(bancoId))
                .filter(p -> p.getCuitTienda().equals(cuitTienda))
                .filter(p -> p.esValida(fechaCompra))
                .toList();

        // Obtener IDs de promociones ya aplicadas
        List<String> promocionesYaAplicadas = compra.getPromocionesAplicadas().stream()
                .map(Promocion::getId)
                .toList();

        // Agregar solo las promociones que no están ya aplicadas
        List<Promocion> promocionesNuevas = promocionesValidas.stream()
                .filter(p -> !promocionesYaAplicadas.contains(p.getId()))
                .toList();

        compra.getPromocionesAplicadas().addAll(promocionesNuevas);
    }

    // Obtener información de una compra con sus detalles
    @Override
    public Compra obtenerCompraConDetalles(String compraId) {
        return compraRepository.findById(compraId)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada con id: " + compraId));
    }

    @Override
    public Compra obtenerCompraPorId(String id) {
        return compraRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Compra no encontrada con id: " + id));
    }

    @Override
    public List<Compra> obtenerTodasLasCompras() {
        return compraRepository.findAll();
    }

    @Override
    public List<Compra> obtenerComprasPorTarjeta(String tarjetaId) {
        return compraRepository.findByTarjetaId(tarjetaId);
    }

    // Obtener el nombre del local con mayor cantidad de compras
    @Override
    public String obtenerLocalConMasCompras() {
        // Usar Aggregation para agrupar por tienda y contar
        GroupOperation groupByTienda = group("tienda").count().as("total");
        SortOperation sortByTotal = sort(org.springframework.data.domain.Sort.Direction.DESC, "total");

        Aggregation aggregation = newAggregation(
                groupByTienda,
                sortByTotal,
                limit(1)
        );

        AggregationResults<org.bson.Document> results = mongoTemplate.aggregate(
                aggregation, "compras", org.bson.Document.class
        );

        if (results.getMappedResults().isEmpty()) {
            throw new NoSuchElementException("No se encontraron compras");
        }

        return results.getMappedResults().get(0).getString("_id");
    }

    public void eliminarCompra(String id) {
        compraRepository.deleteById(id);
    }
}
