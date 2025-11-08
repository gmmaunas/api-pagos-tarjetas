package com.dbd.service.pagos_tarjetas.rest.response;

public record BancoResponse(
        Long id,
        String nombre,
        String cuit,
        String direccion,
        String telefono,
        String direccionWeb
) {}
