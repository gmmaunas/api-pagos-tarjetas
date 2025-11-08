package com.dbd.service.pagos_tarjetas.service.impl;

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
    // Nota: Las promociones pueden haber sido aplicadas a compras, pero se mantiene la referencia
    @Override
    public void eliminarPromocionPorCodigo(String codigo) {
        Promocion promocion = obtenerPromocionPorCodigo(codigo);
        
        // La eliminación en cascada no afectará las compras debido a la relación ManyToMany
        // Las compras mantienen la referencia histórica de las promociones aplicadas
        promocionRepository.delete(promocion);
    }

    @Override
    public void eliminarPromocion(Long id) {
        promocionRepository.deleteById(id);
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
