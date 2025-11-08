package com.dbd.service.pagos_tarjetas.service.impl;

import com.dbd.service.pagos_tarjetas.model.CompraPagoUnico;
import com.dbd.service.pagos_tarjetas.model.Cuota;
import com.dbd.service.pagos_tarjetas.model.Pago;
import com.dbd.service.pagos_tarjetas.repository.CompraPagoUnicoRepository;
import com.dbd.service.pagos_tarjetas.repository.CuotaRepository;
import com.dbd.service.pagos_tarjetas.repository.PagoRepository;
import com.dbd.service.pagos_tarjetas.service.IPagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PagoServiceImpl implements IPagoService {
    
    private final PagoRepository pagoRepository;
    private final CuotaRepository cuotaRepository;
    private final CompraPagoUnicoRepository compraPagoUnicoRepository;

    // Generar el total de pago de un mes dado, informando los items correspondientes
    @Override
    public Pago generarPagoMensual(String mes, String anio, LocalDate primerVencimiento,
                                   LocalDate segundoVencimiento, Double recargoPrimero,
                                   Double recargoSegundo) {

        // Verificar si ya existe un pago para ese mes y año
        pagoRepository.findByMesAndAnio(mes, anio).ifPresent(p -> {
            throw new IllegalArgumentException("Ya existe un pago para el mes " + mes + "/" + anio);
        });

        // Obtener cuotas del mes sin asignar a pago
        List<Cuota> cuotas = cuotaRepository.findCuotasSinPagoPorMesAnio(mes, anio);

        // Obtener compras en un solo pago del mes anterior sin asignar
        int mesInt = Integer.parseInt(mes);
        int anioInt = Integer.parseInt(anio);
        int mesAnterior = mesInt == 1 ? 12 : mesInt - 1;
        int anioAnterior = mesInt == 1 ? anioInt - 1 : anioInt;

        List<CompraPagoUnico> comprasPagoUnico = compraPagoUnicoRepository
                .findComprasSinPagoPorMesAnio(mesAnterior, anioAnterior);

        // Crear el pago
        Pago pago = Pago.builder()
                .codigo("PAG-" + anio + mes)
                .mes(mes)
                .anio(anio)
                .primerVencimiento(primerVencimiento)
                .segundoVencimiento(segundoVencimiento)
                .recargoPrimerVencimiento(recargoPrimero)
                .recargoSegundoVencimiento(recargoSegundo)
                .precioTotal(0.0)
                .build();

        // Asignar cuotas al pago
        for (Cuota cuota : cuotas) {
            cuota.setPago(pago);
            pago.getCuotas().add(cuota);
        }

        // Asignar compras en un solo pago
        for (CompraPagoUnico compra : comprasPagoUnico) {
            compra.setPago(pago);
            pago.getComprasPagoUnico().add(compra);
        }

        // Calcular precio total
        pago.calcularPrecioTotal();

        pagoRepository.save(pago);

        // Cargar promociones de las compras para el mapper
        pagoRepository.findComprasConPromocionesDelPago(pago.getCodigo());

        return pago;
    }

    // Editar las fechas de vencimiento de un pago con cierto código
    @Override
    public Pago editarFechasVencimiento(String codigo, LocalDate nuevoPrimerVencimiento,
                                        LocalDate nuevoSegundoVencimiento) {
        Pago pago = pagoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con código: " + codigo));

        pago.setPrimerVencimiento(nuevoPrimerVencimiento);
        pago.setSegundoVencimiento(nuevoSegundoVencimiento);

        pagoRepository.save(pago);

        // Cargar colecciones para el mapper
        pagoRepository.findByCodigoConCuotas(codigo);
        pagoRepository.findByCodigoConCompras(codigo);
        pagoRepository.findComprasConPromocionesDelPago(codigo);

        return pago;
    }

    @Override
    public Pago obtenerPagoPorCodigoConItems(String codigo) {
        // Primera consulta: cargar pago con cuotas
        Pago pago = pagoRepository.findByCodigoConCuotas(codigo)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con código: " + codigo));

        // Segunda consulta: cargar compras con tarjetas
        pagoRepository.findByCodigoConCompras(codigo);

        // Tercera consulta: cargar promociones de las compras
        pagoRepository.findComprasConPromocionesDelPago(codigo);

        return pago;
    }

    @Override
    public Pago obtenerPagoPorId(Long id) {
        return pagoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pago no encontrado con id: " + id));
    }

    @Override
    public Pago obtenerPagoPorCodigo(String codigo) {
        return pagoRepository.findByCodigo(codigo)
            .orElseThrow(() -> new RuntimeException("Pago no encontrado con código: " + codigo));
    }

    @Override
    public List<Pago> obtenerTodosLosPagos() {
        // Primera consulta: cargar pagos con cuotas
        List<Pago> pagos = pagoRepository.findAllConCuotas();

        // Segunda consulta: cargar compras con tarjetas
        pagoRepository.findAllConCompras();

        // Tercera consulta: cargar promociones de las compras
        pagoRepository.findAllComprasConPromociones();

        return pagos;
    }

    @Override
    public List<Pago> obtenerPagosPorAnio(String anio) {
        return pagoRepository.findByAnio(anio);
    }

    @Override
    public void eliminarPago(Long id) {
        pagoRepository.deleteById(id);
    }
}
