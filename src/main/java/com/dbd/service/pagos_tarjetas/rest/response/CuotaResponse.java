package com.dbd.service.pagos_tarjetas.rest.response;

public record CuotaResponse(
        Long id,
        Integer numero,
        Double precio,
        String mes,
        String anio,
        Long compraId,
        Long pagoId
) {}
