package com.dbd.service.pagos_tarjetas.service.impl;

import com.dbd.service.pagos_tarjetas.model.Banco;
import com.dbd.service.pagos_tarjetas.repository.BancoRepository;
import com.dbd.service.pagos_tarjetas.repository.TitularTarjetaRepository;
import com.dbd.service.pagos_tarjetas.service.IBancoService;
import lombok.RequiredArgsConstructor;
import com.dbd.service.pagos_tarjetas.model.Compra;
import com.dbd.service.pagos_tarjetas.model.TitularTarjeta;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import java.util.Collections;
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
        bancoRepository.findByCuit(banco.getCuit()).ifPresent(b -> {
            throw new IllegalArgumentException("Ya existe un banco con CUIT: " + banco.getCuit());
        });
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
        List<Compra> compras = mongoTemplate.findAll(Compra.class);

        Map<String, Long> comprasPorBanco = new HashMap<>();
        for (Compra compra : compras) {
            if (compra.getTarjeta() != null && compra.getTarjeta().getBanco() != null) {
                String bancoId = compra.getTarjeta().getBanco().getId();
                comprasPorBanco.merge(bancoId, 1L, Long::sum);
            }
        }

        if (comprasPorBanco.isEmpty()) {
            throw new RuntimeException("No se encontraron bancos con compras");
        }

        String topBancoId = Collections.max(comprasPorBanco.entrySet(),
                Map.Entry.comparingByValue()).getKey();
        return bancoRepository.findById(topBancoId)
                .orElseThrow(() -> new RuntimeException("Banco no encontrado con id: " + topBancoId));
    }

    // Obtener el número de clientes de cada banco
    @Override
    public Map<String, Long> obtenerNumeroClientesPorBanco() {
        List<Banco> bancos = bancoRepository.findAll();
        List<TitularTarjeta> todosTitulares = titularTarjetaRepository.findAll();
        Map<String, Long> clientesPorBanco = new HashMap<>();

        for (Banco banco : bancos) {
            long cantidadClientes = todosTitulares.stream()
                    .filter(t -> t.getBancos() != null && t.getBancos().stream()
                            .anyMatch(b -> b.getId().equals(banco.getId())))
                    .count();
            clientesPorBanco.put(banco.getNombre(), cantidadClientes);
        }

        return clientesPorBanco;
    }
}
