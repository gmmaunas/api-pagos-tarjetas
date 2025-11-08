package com.dbd.service.pagos_tarjetas.mapper;

import com.dbd.service.pagos_tarjetas.model.Pago;
import com.dbd.service.pagos_tarjetas.rest.request.PagoRequest;
import com.dbd.service.pagos_tarjetas.rest.response.PagoResponse;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PagoMapper {

    private PagoMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Pago toEntity(PagoRequest request, String codigo) {
        if (request == null) {
            return null;
        }

        return Pago.builder()
                .codigo(codigo)
                .mes(request.mes())
                .anio(request.anio())
                .primerVencimiento(request.primerVencimiento())
                .segundoVencimiento(request.segundoVencimiento())
                .recargoPrimerVencimiento(request.recargoPrimerVencimiento())
                .recargoSegundoVencimiento(request.recargoSegundoVencimiento())
                .precioTotal(0.0)
                .build();
    }

    public static PagoResponse toResponse(Pago pago) {
        if (pago == null) {
            return null;
        }

        return new PagoResponse(
                pago.getId(),
                pago.getCodigo(),
                pago.getMes(),
                pago.getAnio(),
                pago.getPrimerVencimiento(),
                pago.getSegundoVencimiento(),
                pago.getRecargoPrimerVencimiento(),
                pago.getRecargoSegundoVencimiento(),
                pago.getPrecioTotal(),
                pago.getCuotas() != null
                        ? pago.getCuotas().stream()
                        .map(CompraMapper::toCuotaResponse)
                        .collect(Collectors.toList())
                        : new ArrayList<>(),
                pago.getComprasPagoUnico() != null
                        ? pago.getComprasPagoUnico().stream()
                        .map(CompraMapper::toPagoUnicoResponse)
                        .collect(Collectors.toList())
                        : new ArrayList<>()
        );
    }
}
