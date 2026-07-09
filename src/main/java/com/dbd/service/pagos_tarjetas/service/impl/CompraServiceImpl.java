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
        List<Promocion> candidatas = obtenerPromocionesCandiatas(compra);
        Promocion seleccionada = candidatas.stream()
                .filter(Promocion::seAplicaAPagoUnico)
                .findFirst()
                .orElse(null);
        compra.setPromocionAplicada(seleccionada);
        compra.calcularMontoFinal();
        return compraPagoUnicoRepository.save(compra);
    }
    
    // Crear compra en cuotas
    @Override
    public CompraCuotas crearCompraCuotas(CompraCuotas compra) {
        List<Promocion> candidatas = obtenerPromocionesCandiatas(compra);
        // Preferir financiación con mismo número de cuotas; si no, descuento genérico
        Promocion seleccionada = candidatas.stream()
                .filter(p -> p.seAplicaACuotasConNumero(compra.getNumeroCuotas()))
                .findFirst()
                .or(() -> candidatas.stream()
                        .filter(Promocion::seAplicaACuotasComoDescuento)
                        .findFirst())
                .orElse(null);
        compra.setPromocionAplicada(seleccionada);
        compra.calcularMontoFinal();
        compra.generarCuotas();
        return compraCuotasRepository.save(compra);
    }

    // Obtener las promociones candidatas para la compra (mismo banco, tienda y fecha vigente)
    private List<Promocion> obtenerPromocionesCandiatas(Compra compra) {
        Tarjeta tarjeta = tarjetaRepository.findById(compra.getTarjeta().getId())
                .orElseThrow(() -> new RuntimeException("Tarjeta no encontrada"));
        Long bancoId = tarjeta.getBanco().getId();
        LocalDate fechaCompra = compra.getFechaHora().toLocalDate();
        return promocionRepository
                .findPromocionesValidasPorTiendaYFecha(compra.getCuitTienda(), fechaCompra)
                .stream()
                .filter(p -> p.getBanco().getId().equals(bancoId))
                .toList();
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
        return compraRepository.findAllConDetalles();
    }

    @Override
    public List<Compra> obtenerComprasPorTarjeta(Long tarjetaId) {
        return compraRepository.findByTarjetaIdConDetalles(tarjetaId);
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
