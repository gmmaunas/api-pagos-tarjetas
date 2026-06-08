package com.dbd.service.pagos_tarjetas.mapper;

import com.dbd.service.pagos_tarjetas.model.Banco;
import com.dbd.service.pagos_tarjetas.model.TitularTarjeta;
import com.dbd.service.pagos_tarjetas.rest.request.TitularTarjetaRequest;
import com.dbd.service.pagos_tarjetas.rest.response.BancoResponse;
import com.dbd.service.pagos_tarjetas.rest.response.TitularTarjetaResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TitularTarjetaMapper {

    private TitularTarjetaMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static TitularTarjeta toEntity(TitularTarjetaRequest request, List<Banco> bancos) {
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
                .bancos(bancos != null ? new ArrayList<>(bancos) : new ArrayList<>())
                .build();
    }

    public static TitularTarjetaResponse toResponse(TitularTarjeta titular) {
        if (titular == null) {
            return null;
        }

        List<BancoResponse> bancosResponse = titular.getBancos() != null
                ? titular.getBancos().stream()
                        .map(BancoMapper::toResponse)
                        .collect(Collectors.toList())
                : new ArrayList<>();

        return new TitularTarjetaResponse(
                titular.getId(),
                titular.getNombreCompleto(),
                titular.getDni(),
                titular.getCuit(),
                titular.getDireccion(),
                titular.getTelefono(),
                titular.getFechaAlta(),
                bancosResponse
        );
    }

    public static void updateEntityFromRequest(TitularTarjeta titular, TitularTarjetaRequest request, List<Banco> bancos) {
        if (titular == null || request == null) {
            return;
        }

        titular.setNombreCompleto(request.nombreCompleto());
        titular.setDni(request.dni());
        titular.setCuit(request.cuit());
        titular.setDireccion(request.direccion());
        titular.setTelefono(request.telefono());
        titular.setFechaAlta(request.fechaAlta());
        if (bancos != null && !bancos.isEmpty()) {
            titular.getBancos().clear();
            titular.getBancos().addAll(bancos);
        }
    }
}
