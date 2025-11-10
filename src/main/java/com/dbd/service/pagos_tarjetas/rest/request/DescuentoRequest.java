package com.dbd.service.pagos_tarjetas.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

public record DescuentoRequest(
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

        @NotNull(message = "El porcentaje de descuento es obligatorio")
        @PositiveOrZero(message = "El porcentaje debe ser positivo o cero")
        Double porcentajeDescuento,

        @PositiveOrZero(message = "El tope debe ser positivo o cero")
        Double tope,

        Boolean soloContado
) {}
