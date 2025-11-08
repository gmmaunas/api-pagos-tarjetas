package com.dbd.service.pagos_tarjetas.rest.response;

import java.time.LocalDate;

public record FinanciacionResponse(
        Long id,
        String codigo,
        String tituloPromocion,
        String nombreTienda,
        String cuitTienda,
        LocalDate fechaInicioValidez,
        LocalDate fechaFinValidez,
        String comentarios,
        Long bancoId,
        String bancoNombre,
        Integer numeroCuotas,
        Double interes
) {}
