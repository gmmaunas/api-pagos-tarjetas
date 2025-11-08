package com.dbd.service.pagos_tarjetas.service.impl;

import com.dbd.service.pagos_tarjetas.model.*;
import com.dbd.service.pagos_tarjetas.repository.*;
import com.dbd.service.pagos_tarjetas.service.ICompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class CompraServiceImpl implements ICompraService {
    
    private final CompraRepository compraRepository;
    private final CompraPagoUnicoRepository compraPagoUnicoRepository;
    private final CompraCuotasRepository compraCuotasRepository;
    private final PromocionRepository promocionRepository;
    private final TarjetaRepository tarjetaRepository;
    
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

        Long bancoId = tarjeta.getBanco().getId();
        String cuitTienda = compra.getCuitTienda();
        LocalDate fechaCompra = compra.getFechaHora().toLocalDate();

        // Obtener promociones válidas del banco para la tienda y fecha
        List<Promocion> promocionesValidas = promocionRepository
                .findPromocionesValidasPorTiendaYFecha(cuitTienda, fechaCompra)
                .stream()
                .filter(p -> p.getBanco().getId().equals(bancoId))
                .toList();

        // Obtener IDs de promociones ya aplicadas
        List<Long> promocionesYaAplicadas = compra.getPromocionesAplicadas().stream()
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
    public Compra obtenerCompraConDetalles(Long compraId) {
        Compra compra = compraRepository.findByIdConDetalles(compraId)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada con id: " + compraId));

        // Si es compra en cuotas, cargar las cuotas
        return switch (compra) {
            case CompraCuotas cc -> compraCuotasRepository.findByIdConCuotas(compraId)
                    .orElse(cc);
            default -> compra;
        };
    }

    @Override
    public Compra obtenerCompraPorId(Long id) {
        return compraRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Compra no encontrada con id: " + id));
    }

    @Override
    public List<Compra> obtenerTodasLasCompras() {
        return compraRepository.findAll();
    }

    @Override
    public List<Compra> obtenerComprasPorTarjeta(Long tarjetaId) {
        return compraRepository.findByTarjetaId(tarjetaId);
    }
    
    // Obtener el nombre del local con mayor cantidad de compras
    @Override
    public String obtenerLocalConMasCompras() {
        List<Object[]> resultados = compraRepository.findTiendaConMasCompras();
        
        if (resultados.isEmpty()) {
            throw new NoSuchElementException("No se encontraron compras");
        }
        
        return (String) resultados.get(0)[0];
    }
    
    public void eliminarCompra(Long id) {
        compraRepository.deleteById(id);
    }
}
