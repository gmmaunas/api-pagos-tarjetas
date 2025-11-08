package com.dbd.service.pagos_tarjetas.rest.response;

import java.time.LocalDate;

public record TarjetaResponse(
        Long id,
        String numero,
        String ccv,
        String nombreTitularTarjeta,
        LocalDate desde,
        LocalDate fechaVencimiento,
        Long titularId,
        String titularNombre,
        Long bancoId,
        String bancoNombre
) {}
