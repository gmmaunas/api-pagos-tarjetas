package com.dbd.service.pagos_tarjetas.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

public record PagoRequest(
        @NotBlank(message = "El mes es obligatorio")
        String mes,

        @NotBlank(message = "El año es obligatorio")
        String anio,

        @NotNull(message = "La fecha del primer vencimiento es obligatoria")
        LocalDate primerVencimiento,

        @NotNull(message = "La fecha del segundo vencimiento es obligatoria")
        LocalDate segundoVencimiento,

        @NotNull(message = "El recargo del primer vencimiento es obligatorio")
        @PositiveOrZero(message = "El recargo debe ser positivo o cero")
        Double recargoPrimerVencimiento,

        @NotNull(message = "El recargo del segundo vencimiento es obligatorio")
        @PositiveOrZero(message = "El recargo debe ser positivo o cero")
        Double recargoSegundoVencimiento
) {}
