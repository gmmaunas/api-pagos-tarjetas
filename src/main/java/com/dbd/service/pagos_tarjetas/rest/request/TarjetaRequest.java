package com.dbd.service.pagos_tarjetas.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record TarjetaRequest(
        @NotBlank(message = "El número de tarjeta es obligatorio")
        String numero,

        @NotBlank(message = "El CCV es obligatorio")
        String ccv,

        @NotBlank(message = "El nombre del titular en la tarjeta es obligatorio")
        String nombreTitularTarjeta,

        @NotNull(message = "La fecha 'desde' es obligatoria")
        LocalDate desde,

        @NotNull(message = "La fecha de vencimiento es obligatoria")
        LocalDate fechaVencimiento,

        @NotNull(message = "El ID del titular es obligatorio")
        String titularId,

        @NotNull(message = "El ID del banco es obligatorio")
        String bancoId
) {}
