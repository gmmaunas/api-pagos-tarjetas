package com.dbd.service.pagos_tarjetas.service.impl;

import com.dbd.service.pagos_tarjetas.model.Compra;
import com.dbd.service.pagos_tarjetas.model.Descuento;
import com.dbd.service.pagos_tarjetas.model.Financiacion;
import com.dbd.service.pagos_tarjetas.model.Promocion;
import com.dbd.service.pagos_tarjetas.repository.DescuentoRepository;
import com.dbd.service.pagos_tarjetas.repository.FinanciacionRepository;
import com.dbd.service.pagos_tarjetas.repository.PromocionRepository;
import com.dbd.service.pagos_tarjetas.service.IPromocionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PromocionServiceImpl implements IPromocionService {
    
    private final PromocionRepository promocionRepository;
    private final DescuentoRepository descuentoRepository;
    private final FinanciacionRepository financiacionRepository;
    
    // Agregar una nueva promoción de tipo descuento a un banco dado
    @Override
    public Descuento agregarDescuento(Descuento descuento) {
        return descuentoRepository.save(descuento);
    }
    
    // Agregar una nueva promoción de tipo financiación
    @Override
    public Financiacion agregarFinanciacion(Financiacion financiacion) {
        return financiacionRepository.save(financiacion);
    }

    @Override
    public Promocion obtenerPromocionPorId(Long id) {
        return promocionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Promoción no encontrada con id: " + id));
    }

    @Override
    public Promocion obtenerPromocionPorCodigo(String codigo) {
        return promocionRepository.findByCodigoConBanco(codigo)
                .orElseThrow(() -> new RuntimeException("Promoción no encontrada con código: " + codigo));
    }

    @Override
    public List<Promocion> obtenerTodasLasPromociones() {
        return promocionRepository.findAll();
    }

    @Override
    public List<Promocion> obtenerPromocionesPorBanco(Long bancoId) {
        return promocionRepository.findByBancoId(bancoId);
    }
    
    // Obtener el listado de las promociones disponibles de un local entre dos fechas
    @Override
    public List<Promocion> obtenerPromocionesDisponiblesPorLocalYFechas(
            String cuitTienda, 
            LocalDate fechaInicio, 
            LocalDate fechaFin) {
        return promocionRepository.findPromocionesDisponiblesPorLocalYFechas(
            cuitTienda, fechaInicio, fechaFin);
    }
    
    // Eliminar una promoción a través de su código
    @Override
    public void eliminarPromocionPorCodigo(String codigo) {
        Promocion promocion = promocionRepository.findByCodigoConCompras(codigo)
            .orElseThrow(() -> new RuntimeException("Promoci\u00f3n no encontrada con c\u00f3digo: " + codigo));

        for (Compra compra : new ArrayList<>(promocion.getCompras())) {
            compra.setPromocionAplicada(null);
        }

        promocionRepository.delete(promocion);
    }

    @Override
    public void eliminarPromocion(Long id) {
        Promocion promocion = promocionRepository.findByIdConCompras(id)
            .orElseThrow(() -> new RuntimeException("Promoci\u00f3n no encontrada con id: " + id));

        for (Compra compra : new ArrayList<>(promocion.getCompras())) {
            compra.setPromocionAplicada(null);
        }

        promocionRepository.delete(promocion);
    }

    @Override
    public Promocion actualizarPromocion(Long id, Promocion promocionActualizada) {
        Promocion promocion = obtenerPromocionPorId(id);
        promocion.setTituloPromocion(promocionActualizada.getTituloPromocion());
        promocion.setFechaInicioValidez(promocionActualizada.getFechaInicioValidez());
        promocion.setFechaFinValidez(promocionActualizada.getFechaFinValidez());
        promocion.setComentarios(promocionActualizada.getComentarios());
        return promocionRepository.save(promocion);
    }
}
