package com.dbd.service.pagos_tarjetas.mapper;

import com.dbd.service.pagos_tarjetas.model.Banco;
import com.dbd.service.pagos_tarjetas.model.Tarjeta;
import com.dbd.service.pagos_tarjetas.model.TitularTarjeta;
import com.dbd.service.pagos_tarjetas.rest.request.TarjetaRequest;
import com.dbd.service.pagos_tarjetas.rest.response.TarjetaResponse;

public class TarjetaMapper {

    private TarjetaMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Tarjeta toEntity(TarjetaRequest request, TitularTarjeta titular, Banco banco) {
        if (request == null) {
            return null;
        }

        return Tarjeta.builder()
                .numero(request.numero())
                .ccv(request.ccv())
                .nombreTitularTarjeta(request.nombreTitularTarjeta())
                .desde(request.desde())
                .fechaVencimiento(request.fechaVencimiento())
                .titularTarjeta(titular)
                .banco(banco)
                .build();
    }

    public static TarjetaResponse toResponse(Tarjeta tarjeta) {
        if (tarjeta == null) {
            return null;
        }

        return new TarjetaResponse(
                tarjeta.getId(),
                tarjeta.getNumero(),
                tarjeta.getCcv(),
                tarjeta.getNombreTitularTarjeta(),
                tarjeta.getDesde(),
                tarjeta.getFechaVencimiento(),
                tarjeta.getTitularTarjeta() != null ? tarjeta.getTitularTarjeta().getId() : null,
                tarjeta.getTitularTarjeta() != null ? tarjeta.getTitularTarjeta().getNombreCompleto() : null,
                tarjeta.getBanco() != null ? tarjeta.getBanco().getId() : null,
                tarjeta.getBanco() != null ? tarjeta.getBanco().getNombre() : null
        );
    }

    public static void updateEntityFromRequest(Tarjeta tarjeta, TarjetaRequest request, TitularTarjeta titular, Banco banco) {
        if (tarjeta == null || request == null) {
            return;
        }

        tarjeta.setNumero(request.numero());
        tarjeta.setCcv(request.ccv());
        tarjeta.setNombreTitularTarjeta(request.nombreTitularTarjeta());
        tarjeta.setDesde(request.desde());
        tarjeta.setFechaVencimiento(request.fechaVencimiento());
        if (titular != null) {
            tarjeta.setTitularTarjeta(titular);
        }
        if (banco != null) {
            tarjeta.setBanco(banco);
        }
    }
}
