package com.dbd.service.pagos_tarjetas.rest.response;

import java.time.LocalDateTime;
import java.util.List;

public record CompraResponse(
        String id,
        String tipo,
        String comprobanteVoucher,
        String tienda,
        String cuitTienda,
        Double monto,
        Double montoFinal,
        LocalDateTime fechaHora,
        String tarjetaId,
        String tarjetaNumero,
        List<PromocionResponse> promocionesAplicadas
) {}
