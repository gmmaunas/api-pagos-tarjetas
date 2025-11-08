package com.dbd.service.pagos_tarjetas.rest.response;

import java.time.LocalDateTime;
import java.util.List;

public record CompraPagoUnicoResponse(
        Long id,
        String comprobanteVoucher,
        String tienda,
        String cuitTienda,
        Double monto,
        Double montoFinal,
        LocalDateTime fechaHora,
        Long tarjetaId,
        String tarjetaNumero,
        Double descuentoTienda,
        Long pagoId,
        List<PromocionResponse> promocionesAplicadas
) {}
