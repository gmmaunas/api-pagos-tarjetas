package com.dbd.service.pagos_tarjetas.service.impl;

import com.dbd.service.pagos_tarjetas.model.TitularTarjeta;
import com.dbd.service.pagos_tarjetas.repository.TitularTarjetaRepository;
import com.dbd.service.pagos_tarjetas.service.ITitularTarjetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TitularTarjetaServiceImpl implements ITitularTarjetaService {

    private final TitularTarjetaRepository titularTarjetaRepository;
    private final MongoTemplate mongoTemplate;

    public TitularTarjeta crearTitular(TitularTarjeta titular) {
        return titularTarjetaRepository.save(titular);
    }

    @Override
    public TitularTarjeta obtenerTitularPorId(String id) {
        return titularTarjetaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Titular no encontrado con id: " + id));
    }

    @Override
    public TitularTarjeta obtenerTitularPorCuit(String cuit) {
        return titularTarjetaRepository.findByCuit(cuit)
            .orElseThrow(() -> new RuntimeException("Titular no encontrado con CUIT: " + cuit));
    }

    @Override
    public TitularTarjeta obtenerTitularPorDni(String dni) {
        return titularTarjetaRepository.findByDni(dni)
            .orElseThrow(() -> new RuntimeException("Titular no encontrado con DNI: " + dni));
    }

    @Override
    public List<TitularTarjeta> obtenerTodosLosTitulares() {
        return titularTarjetaRepository.findAll();
    }

    @Override
    public List<TitularTarjeta> obtenerTitularesPorBanco(String bancoId) {
        return titularTarjetaRepository.findByBancoId(bancoId);
    }

    @Override
    public TitularTarjeta actualizarTitular(String id, TitularTarjeta titularActualizado) {
        TitularTarjeta titular = obtenerTitularPorId(id);
        titular.setNombreCompleto(titularActualizado.getNombreCompleto());
        titular.setDireccion(titularActualizado.getDireccion());
        titular.setTelefono(titularActualizado.getTelefono());
        return titularTarjetaRepository.save(titular);
    }

    @Override
    public void eliminarTitular(String id) {
        titularTarjetaRepository.deleteById(id);
    }

    // Obtener los nombres de los N titulares con mayor monto total en compras
    @Override
    public Map<String, Double> obtenerTopNTitularesConMayorMontoCompras(int limite) {

        Aggregation aggregation = Aggregation.newAggregation(
                // Desde la colección de compras
                Aggregation.lookup("tarjetas", "tarjeta.$id", "_id", "tarjeta_info"),
                Aggregation.unwind("tarjeta_info"),

                // Unir con titulares
                Aggregation.lookup("titulares_tarjeta", "tarjeta_info.titularTarjeta.$id", "_id", "titular_info"),
                Aggregation.unwind("titular_info"),

                // Agrupar por titular y sumar montos finales
                Aggregation.group("titular_info.nombreCompleto")
                        .sum("montoFinal").as("montoTotal"),

                // Ordenar por monto total descendente
                Aggregation.sort(Sort.Direction.DESC, "montoTotal"),

                // Limitar resultados
                Aggregation.limit(limite)
        );

        AggregationResults<Map> results = mongoTemplate.aggregate(
                aggregation, "compras", Map.class
        );

        // Convertir resultados a Map<String, Double>
        Map<String, Double> topTitulares = new LinkedHashMap<>();
        for (Map result : results.getMappedResults()) {
            String nombreTitular = (String) result.get("_id");
            Number montoTotal = (Number) result.get("montoTotal");
            topTitulares.put(nombreTitular, montoTotal.doubleValue());
        }

        return topTitulares;
    }
}
