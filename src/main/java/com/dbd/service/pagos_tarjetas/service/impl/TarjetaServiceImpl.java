package com.dbd.service.pagos_tarjetas.service.impl;

import com.dbd.service.pagos_tarjetas.model.Tarjeta;
import com.dbd.service.pagos_tarjetas.repository.TarjetaRepository;
import com.dbd.service.pagos_tarjetas.service.ITarjetaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TarjetaServiceImpl implements ITarjetaService {
    
    private final TarjetaRepository tarjetaRepository;
    
    public Tarjeta crearTarjeta(Tarjeta tarjeta) {
        return tarjetaRepository.save(tarjeta);
    }

    @Override
    public Tarjeta obtenerTarjetaPorId(Long id) {
        return tarjetaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada con id: " + id));
    }

    @Override
    public Tarjeta obtenerTarjetaPorNumero(String numero) {
        return tarjetaRepository.findByNumero(numero)
            .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada con número: " + numero));
    }

    @Override
    public List<Tarjeta> obtenerTodasLasTarjetas() {
        return tarjetaRepository.findAllWithTitularAndBanco();
    }

    @Override
    public List<Tarjeta> obtenerTarjetasPorTitular(Long titularId) {
        return tarjetaRepository.findByTitularTarjetaId(titularId);
    }

    @Override
    public List<Tarjeta> obtenerTarjetasPorBanco(Long bancoId) {
        return tarjetaRepository.findByBancoId(bancoId);
    }

    @Override
    public Tarjeta actualizarTarjeta(Long id, Tarjeta tarjetaActualizada) {
        Tarjeta tarjeta = obtenerTarjetaPorId(id);
        tarjeta.setCcv(tarjetaActualizada.getCcv());
        tarjeta.setFechaVencimiento(tarjetaActualizada.getFechaVencimiento());
        return tarjetaRepository.save(tarjeta);
    }

    @Override
    public void eliminarTarjeta(Long id) {
        tarjetaRepository.deleteById(id);
    }
    
    // Obtener el listado de tarjetas emitidas hace más de N años
    @Override
    public List<Tarjeta> obtenerTarjetasEmitidasHaceMasDeNAnios(int anios) {
        LocalDate fechaLimite = LocalDate.now().minusYears(anios);
        return tarjetaRepository.findTarjetasEmitidasHaceMasDeNAnios(fechaLimite);
    }
}
