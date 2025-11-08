package com.dbd.service.pagos_tarjetas.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

public record CompraCuotasRequest(
        @NotBlank(message = "El comprobante/voucher es obligatorio")
        String comprobanteVoucher,

        @NotBlank(message = "El nombre de la tienda es obligatorio")
        String tienda,

        @NotBlank(message = "El CUIT de la tienda es obligatorio")
        String cuitTienda,

        @NotNull(message = "El monto es obligatorio")
        @PositiveOrZero(message = "El monto debe ser positivo o cero")
        Double monto,

        @NotNull(message = "La fecha y hora son obligatorias")
        LocalDateTime fechaHora,

        @NotNull(message = "El ID de la tarjeta es obligatorio")
        Long tarjetaId,

        @PositiveOrZero(message = "El interés debe ser positivo o cero")
        Double interes,

        @NotNull(message = "El número de cuotas es obligatorio")
        @Positive(message = "El número de cuotas debe ser mayor a cero")
        Integer numeroCuotas,

        List<String> codigosPromociones
) {}
