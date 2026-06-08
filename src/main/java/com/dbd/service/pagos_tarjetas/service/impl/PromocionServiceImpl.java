package com.dbd.service.pagos_tarjetas.service.impl;

import com.dbd.service.pagos_tarjetas.model.Compra;
import com.dbd.service.pagos_tarjetas.model.Descuento;
import com.dbd.service.pagos_tarjetas.model.Financiacion;
import com.dbd.service.pagos_tarjetas.model.Promocion;
import com.dbd.service.pagos_tarjetas.repository.CompraRepository;
import com.dbd.service.pagos_tarjetas.repository.DescuentoRepository;
import com.dbd.service.pagos_tarjetas.repository.FinanciacionRepository;
import com.dbd.service.pagos_tarjetas.repository.PromocionRepository;
import com.dbd.service.pagos_tarjetas.service.IPromocionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromocionServiceImpl implements IPromocionService {

    private final PromocionRepository promocionRepository;
    private final DescuentoRepository descuentoRepository;
    private final FinanciacionRepository financiacionRepository;
    private final CompraRepository compraRepository;

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
    public Promocion obtenerPromocionPorId(String id) {
        return promocionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Promoción no encontrada con id: " + id));
    }

    @Override
    public Promocion obtenerPromocionPorCodigo(String codigo) {
        return promocionRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Promoción no encontrada con código: " + codigo));
    }

    @Override
    public List<Promocion> obtenerTodasLasPromociones() {
        return promocionRepository.findAll();
    }

    @Override
    public List<Promocion> obtenerPromocionesPorBanco(String bancoId) {
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
        Promocion promocion = obtenerPromocionPorCodigo(codigo);
        desvincularCompras(promocion.getId());
        promocionRepository.delete(promocion);
    }

    @Override
    public void eliminarPromocion(String id) {
        desvincularCompras(id);
        promocionRepository.deleteById(id);
    }

    private void desvincularCompras(String promocionId) {
        List<Compra> comprasConPromocion = compraRepository.findAll().stream()
                .filter(c -> c.getPromocionesAplicadas() != null &&
                        c.getPromocionesAplicadas().stream()
                                .anyMatch(p -> p.getId().equals(promocionId)))
                .toList();

        for (Compra compra : comprasConPromocion) {
            compra.getPromocionesAplicadas().removeIf(p -> p.getId().equals(promocionId));
            compraRepository.save(compra);
        }
    }

    @Override
    public Promocion actualizarPromocion(String id, Promocion promocionActualizada) {
        Promocion promocion = obtenerPromocionPorId(id);
        promocion.setTituloPromocion(promocionActualizada.getTituloPromocion());
        promocion.setFechaInicioValidez(promocionActualizada.getFechaInicioValidez());
        promocion.setFechaFinValidez(promocionActualizada.getFechaFinValidez());
        promocion.setComentarios(promocionActualizada.getComentarios());
        return promocionRepository.save(promocion);
    }
}
