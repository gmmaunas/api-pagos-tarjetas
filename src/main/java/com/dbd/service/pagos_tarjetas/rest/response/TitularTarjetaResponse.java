package com.dbd.service.pagos_tarjetas.rest.response;

import java.time.LocalDate;

public record TitularTarjetaResponse(
        String id,
        String nombreCompleto,
        String dni,
        String cuit,
        String direccion,
        String telefono,
        LocalDate fechaAlta,
        BancoResponse banco
) {}
