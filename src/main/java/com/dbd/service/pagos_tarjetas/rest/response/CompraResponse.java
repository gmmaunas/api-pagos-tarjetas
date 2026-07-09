package com.dbd.service.pagos_tarjetas.rest.response;

import java.time.LocalDateTime;

public record CompraResponse(
        Long id,
        String tipo,
        String comprobanteVoucher,
        String tienda,
        String cuitTienda,
        Double monto,
        Double montoFinal,
        LocalDateTime fechaHora,
        Long tarjetaId,
        String tarjetaNumero,
        PromocionResponse promocionAplicada
) {}
