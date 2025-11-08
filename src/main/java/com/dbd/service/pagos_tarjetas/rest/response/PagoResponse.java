package com.dbd.service.pagos_tarjetas.rest.response;

import java.time.LocalDate;
import java.util.List;

public record PagoResponse(
        Long id,
        String codigo,
        String mes,
        String anio,
        LocalDate primerVencimiento,
        LocalDate segundoVencimiento,
        Double recargoPrimerVencimiento,
        Double recargoSegundoVencimiento,
        Double precioTotal,
        List<CuotaResponse> cuotas,
        List<CompraPagoUnicoResponse> comprasPagoUnico
) {}
