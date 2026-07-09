package com.dbd.service.pagos_tarjetas.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record PromocionUpdateRequest(
        @NotBlank String tituloPromocion,
        @NotNull LocalDate fechaInicioValidez,
        @NotNull LocalDate fechaFinValidez,
        String comentarios
) {}
