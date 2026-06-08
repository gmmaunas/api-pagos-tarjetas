package com.dbd.service.pagos_tarjetas.rest.response;

import java.time.LocalDate;
import java.util.List;

public record TitularTarjetaResponse(
        String id,
        String nombreCompleto,
        String dni,
        String cuit,
        String direccion,
        String telefono,
        LocalDate fechaAlta,
        List<BancoResponse> bancos
) {}
