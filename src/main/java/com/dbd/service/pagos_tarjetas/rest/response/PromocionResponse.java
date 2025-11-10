package com.dbd.service.pagos_tarjetas.rest.response;

import java.time.LocalDate;

public record PromocionResponse(
        String id,
        String tipo,
        String codigo,
        String tituloPromocion,
        String nombreTienda,
        String cuitTienda,
        LocalDate fechaInicioValidez,
        LocalDate fechaFinValidez,
        String comentarios,
        String bancoId,
        String bancoNombre
) {}
