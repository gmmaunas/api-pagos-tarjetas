package com.dbd.service.pagos_tarjetas.rest.response;

public record CuotaResponse(
        Integer numero,
        Double precio,
        String mes,
        String anio,
        String pagoId
) {}
