package com.dbd.service.pagos_tarjetas.service.impl;

import com.dbd.service.pagos_tarjetas.model.Banco;
import com.dbd.service.pagos_tarjetas.repository.BancoRepository;
import com.dbd.service.pagos_tarjetas.repository.TitularTarjetaRepository;
import com.dbd.service.pagos_tarjetas.service.IBancoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BancoServiceImpl implements IBancoService {

    private final BancoRepository bancoRepository;
    private final TitularTarjetaRepository titularTarjetaRepository;
    private final MongoTemplate mongoTemplate;

    public Banco crearBanco(Banco banco) {
        return bancoRepository.save(banco);
    }

    @Override
    public Banco obtenerBancoPorId(String id) {
        return bancoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Banco no encontrado con id: " + id));
    }

    @Override
    public Banco obtenerBancoPorCuit(String cuit) {
        return bancoRepository.findByCuit(cuit)
            .orElseThrow(() -> new RuntimeException("Banco no encontrado con CUIT: " + cuit));
    }

    @Override
    public List<Banco> obtenerTodosLosBancos() {
        return bancoRepository.findAll();
    }

    @Override
    public Banco actualizarBanco(String id, Banco bancoActualizado) {
        Banco banco = obtenerBancoPorId(id);
        banco.setNombre(bancoActualizado.getNombre());
        banco.setDireccion(bancoActualizado.getDireccion());
        banco.setTelefono(bancoActualizado.getTelefono());
        banco.setDireccionWeb(bancoActualizado.getDireccionWeb());
        return bancoRepository.save(banco);
    }

    @Override
    public void eliminarBanco(String id) {
        bancoRepository.deleteById(id);
    }

    // Obtener el banco con mayor cantidad de compras realizadas con sus tarjetas
    @Override
    public Banco obtenerBancoConMasCompras() {

        Aggregation aggregation = Aggregation.newAggregation(
                // Desde la colección de compras
                Aggregation.lookup("tarjetas", "tarjeta.$id", "_id", "tarjeta_info"),
                Aggregation.unwind("tarjeta_info", true),

                // Unir con bancos
                Aggregation.lookup("bancos", "tarjeta_info.banco.$id", "_id", "banco_info"),
                Aggregation.unwind("banco_info", true),

                // Filtrar documentos sin tarjeta o banco
                Aggregation.match(Criteria.where("banco_info").exists(true)),

                // Agrupar por banco y contar compras
                Aggregation.group("banco_info._id")
                        .count().as("cantidadCompras"),

                // Ordenar por cantidad de compras descendente
                Aggregation.sort(Sort.Direction.DESC, "cantidadCompras"),

                // Tomar solo el primero
                Aggregation.limit(1)
        );

        AggregationResults<Map> results = mongoTemplate.aggregate(
                aggregation, "compras", Map.class
        );

        if (results.getMappedResults().isEmpty()) {
            throw new RuntimeException("No se encontraron bancos con compras");
        }

        // Extraer el ID del banco del resultado
        Map result = results.getMappedResults().get(0);
        String bancoId = result.get("_id").toString();

        // Obtener el banco completo por ID
        return bancoRepository.findById(bancoId)
                .orElseThrow(() -> new RuntimeException("Banco no encontrado con id: " + bancoId));
    }

    // Obtener el número de clientes de cada banco
    @Override
    public Map<String, Long> obtenerNumeroClientesPorBanco() {
        List<Banco> bancos = bancoRepository.findAll();
        Map<String, Long> clientesPorBanco = new HashMap<>();

        for (Banco banco : bancos) {
            long cantidadClientes = titularTarjetaRepository.findByBancoId(banco.getId()).size();
            clientesPorBanco.put(banco.getNombre(), cantidadClientes);
        }

        return clientesPorBanco;
    }
}
