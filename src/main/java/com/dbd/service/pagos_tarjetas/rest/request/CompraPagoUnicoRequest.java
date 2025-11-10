package com.dbd.service.pagos_tarjetas.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

public record CompraPagoUnicoRequest(
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
        String tarjetaId,

        @PositiveOrZero(message = "El descuento de tienda debe ser positivo o cero")
        Double descuentoTienda,

        List<String> codigosPromociones
) {}
