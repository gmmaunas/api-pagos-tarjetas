package com.dbd.service.pagos_tarjetas.rest.response;

import java.time.LocalDate;

public record TarjetaResponse(
        String id,
        String numero,
        String ccv,
        String nombreTitularTarjeta,
        LocalDate desde,
        LocalDate fechaVencimiento,
        String titularId,
        String titularNombre,
        String bancoId,
        String bancoNombre
) {}
