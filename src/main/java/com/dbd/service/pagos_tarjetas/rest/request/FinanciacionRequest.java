package com.dbd.service.pagos_tarjetas.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

public record FinanciacionRequest(
        @NotBlank(message = "El código es obligatorio")
        String codigo,

        @NotBlank(message = "El título de la promoción es obligatorio")
        String tituloPromocion,

        @NotBlank(message = "El nombre de la tienda es obligatorio")
        String nombreTienda,

        @NotBlank(message = "El CUIT de la tienda es obligatorio")
        String cuitTienda,

        @NotNull(message = "La fecha de inicio de validez es obligatoria")
        LocalDate fechaInicioValidez,

        @NotNull(message = "La fecha de fin de validez es obligatoria")
        LocalDate fechaFinValidez,

        String comentarios,

        @NotNull(message = "El ID del banco es obligatorio")
        String bancoId,

        @NotNull(message = "El número de cuotas es obligatorio")
        @Positive(message = "El número de cuotas debe ser mayor a cero")
        Integer numeroCuotas,

        @NotNull(message = "El interés es obligatorio")
        @PositiveOrZero(message = "El interés debe ser positivo o cero")
        Double interes
) {}
