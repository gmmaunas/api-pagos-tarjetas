package com.dbd.service.pagos_tarjetas.rest.response;

public record BancoResponse(
        String id,
        String nombre,
        String cuit,
        String direccion,
        String telefono,
        String direccionWeb
) {}
