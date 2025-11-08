package com.dbd.service.pagos_tarjetas.mapper;

import com.dbd.service.pagos_tarjetas.model.Banco;
import com.dbd.service.pagos_tarjetas.model.TitularTarjeta;
import com.dbd.service.pagos_tarjetas.rest.request.TitularTarjetaRequest;
import com.dbd.service.pagos_tarjetas.rest.response.TitularTarjetaResponse;

public class TitularTarjetaMapper {

    private TitularTarjetaMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static TitularTarjeta toEntity(TitularTarjetaRequest request, Banco banco) {
        if (request == null) {
            return null;
        }

        return TitularTarjeta.builder()
                .nombreCompleto(request.nombreCompleto())
                .dni(request.dni())
                .cuit(request.cuit())
                .direccion(request.direccion())
                .telefono(request.telefono())
                .fechaAlta(request.fechaAlta())
                .banco(banco)
                .build();
    }

    public static TitularTarjetaResponse toResponse(TitularTarjeta titular) {
        if (titular == null) {
            return null;
        }

        return new TitularTarjetaResponse(
                titular.getId(),
                titular.getNombreCompleto(),
                titular.getDni(),
                titular.getCuit(),
                titular.getDireccion(),
                titular.getTelefono(),
                titular.getFechaAlta(),
                BancoMapper.toResponse(titular.getBanco())
        );
    }

    public static void updateEntityFromRequest(TitularTarjeta titular, TitularTarjetaRequest request, Banco banco) {
        if (titular == null || request == null) {
            return;
        }

        titular.setNombreCompleto(request.nombreCompleto());
        titular.setDni(request.dni());
        titular.setCuit(request.cuit());
        titular.setDireccion(request.direccion());
        titular.setTelefono(request.telefono());
        titular.setFechaAlta(request.fechaAlta());
        if (banco != null) {
            titular.setBanco(banco);
        }
    }
}
