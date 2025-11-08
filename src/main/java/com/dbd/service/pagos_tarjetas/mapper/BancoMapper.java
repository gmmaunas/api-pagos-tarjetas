package com.dbd.service.pagos_tarjetas.mapper;

import com.dbd.service.pagos_tarjetas.model.Banco;
import com.dbd.service.pagos_tarjetas.rest.request.BancoRequest;
import com.dbd.service.pagos_tarjetas.rest.response.BancoResponse;

public class BancoMapper {

    private BancoMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static Banco toEntity(BancoRequest request) {
        if (request == null) {
            return null;
        }

        return Banco.builder()
                .nombre(request.nombre())
                .cuit(request.cuit())
                .direccion(request.direccion())
                .telefono(request.telefono())
                .direccionWeb(request.direccionWeb())
                .build();
    }

    public static BancoResponse toResponse(Banco banco) {
        if (banco == null) {
            return null;
        }

        return new BancoResponse(
                banco.getId(),
                banco.getNombre(),
                banco.getCuit(),
                banco.getDireccion(),
                banco.getTelefono(),
                banco.getDireccionWeb()
        );
    }

    public static void updateEntityFromRequest(Banco banco, BancoRequest request) {
        if (banco == null || request == null) {
            return;
        }

        banco.setNombre(request.nombre());
        banco.setCuit(request.cuit());
        banco.setDireccion(request.direccion());
        banco.setTelefono(request.telefono());
        banco.setDireccionWeb(request.direccionWeb());
    }
}
