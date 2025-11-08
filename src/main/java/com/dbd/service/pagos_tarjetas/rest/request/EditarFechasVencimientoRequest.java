package com.dbd.service.pagos_tarjetas.rest.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record EditarFechasVencimientoRequest(
        @NotNull(message = "La fecha del primer vencimiento es obligatoria")
        LocalDate primerVencimiento,

        @NotNull(message = "La fecha del segundo vencimiento es obligatoria")
        LocalDate segundoVencimiento
) {}
