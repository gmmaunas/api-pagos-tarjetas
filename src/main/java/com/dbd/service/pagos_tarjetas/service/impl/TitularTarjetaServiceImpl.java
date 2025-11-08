package com.dbd.service.pagos_tarjetas.service.impl;

import com.dbd.service.pagos_tarjetas.model.TitularTarjeta;
import com.dbd.service.pagos_tarjetas.repository.TitularTarjetaRepository;
import com.dbd.service.pagos_tarjetas.service.ITitularTarjetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

@Service
@RequiredArgsConstructor
@Transactional
public class TitularTarjetaServiceImpl implements ITitularTarjetaService {
    
    private final TitularTarjetaRepository titularTarjetaRepository;
    
    public TitularTarjeta crearTitular(TitularTarjeta titular) {
        return titularTarjetaRepository.save(titular);
    }

    @Override
    public TitularTarjeta obtenerTitularPorId(Long id) {
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
        return titularTarjetaRepository.findAllWithBanco();
    }

    @Override
    public List<TitularTarjeta> obtenerTitularesPorBanco(Long bancoId) {
        return titularTarjetaRepository.findByBancoId(bancoId);
    }

    @Override
    public TitularTarjeta actualizarTitular(Long id, TitularTarjeta titularActualizado) {
        TitularTarjeta titular = obtenerTitularPorId(id);
        titular.setNombreCompleto(titularActualizado.getNombreCompleto());
        titular.setDireccion(titularActualizado.getDireccion());
        titular.setTelefono(titularActualizado.getTelefono());
        return titularTarjetaRepository.save(titular);
    }

    @Override
    public void eliminarTitular(Long id) {
        titularTarjetaRepository.deleteById(id);
    }
    
    // Obtener los nombres de los N titulares con mayor monto total en compras
    @Override
    public Map<String, Double> obtenerTopNTitularesConMayorMontoCompras(int limite) {
        List<Object[]> resultados = titularTarjetaRepository.findTopNTitularesConMayorMontoCompras(limite);
        Map<String, Double> topN = new LinkedHashMap<>();

        int count = 0;
        for (Object[] resultado : resultados) {
            if (count >= limite) break;
            String nombreTitular = (String) resultado[0];
            Double montoTotal = (Double) resultado[1];
            topN.put(nombreTitular, montoTotal);
            count++;
        }

        return topN;
    }
}
