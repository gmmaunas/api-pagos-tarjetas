package com.dbd.service.pagos_tarjetas.service.impl;

import com.dbd.service.pagos_tarjetas.model.TitularTarjeta;
import com.dbd.service.pagos_tarjetas.repository.TitularTarjetaRepository;
import com.dbd.service.pagos_tarjetas.service.ITitularTarjetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import com.dbd.service.pagos_tarjetas.model.Compra;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        return titularTarjetaRepository.findAll().stream()
                .filter(t -> t.getBancos() != null && t.getBancos().stream()
                        .anyMatch(b -> b.getId().equals(bancoId)))
                .collect(Collectors.toList());
    }

    @Override
    public TitularTarjeta actualizarTitular(String id, TitularTarjeta titularActualizado) {
        TitularTarjeta titular = obtenerTitularPorId(id);
        titular.setNombreCompleto(titularActualizado.getNombreCompleto());
        titular.setDireccion(titularActualizado.getDireccion());
        titular.setTelefono(titularActualizado.getTelefono());
        if (titularActualizado.getBancos() != null) {
            titular.setBancos(new ArrayList<>(titularActualizado.getBancos()));
        }
        return titularTarjetaRepository.save(titular);
    }

    @Override
    public void eliminarTitular(String id) {
        titularTarjetaRepository.deleteById(id);
    }

    // Obtener los nombres de los N titulares con mayor monto total en compras
    @Override
    public Map<String, Double> obtenerTopNTitularesConMayorMontoCompras(int limite) {

        List<Compra> compras = mongoTemplate.findAll(Compra.class);
        Map<String, Double> montosPorTitular = new HashMap<>();

        for (Compra compra : compras) {
            if (compra.getTarjeta() != null && compra.getTarjeta().getTitularTarjeta() != null) {
                String nombre = compra.getTarjeta().getTitularTarjeta().getNombreCompleto();
                Double montoFinal = compra.getMontoFinal() != null ? compra.getMontoFinal() : 0.0;
                montosPorTitular.merge(nombre, montoFinal, Double::sum);
            }
        }

        return montosPorTitular.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .limit(limite)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }
}
