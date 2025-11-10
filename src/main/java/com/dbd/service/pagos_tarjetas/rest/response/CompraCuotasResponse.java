package com.dbd.service.pagos_tarjetas.rest.response;

import java.time.LocalDateTime;
import java.util.List;

public record CompraCuotasResponse(
        String id,
        String comprobanteVoucher,
        String tienda,
        String cuitTienda,
        Double monto,
        Double montoFinal,
        LocalDateTime fechaHora,
        String tarjetaId,
        String tarjetaNumero,
        Double interes,
        Integer numeroCuotas,
        List<CuotaResponse> cuotas,
        List<PromocionResponse> promocionesAplicadas
) {}
