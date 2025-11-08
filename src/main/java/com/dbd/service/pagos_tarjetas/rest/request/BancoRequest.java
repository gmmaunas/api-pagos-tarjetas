package com.dbd.service.pagos_tarjetas.rest.request;

import jakarta.validation.constraints.NotBlank;

public record BancoRequest(
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotBlank(message = "El CUIT es obligatorio")
        String cuit,

        @NotBlank(message = "La dirección es obligatoria")
        String direccion,

        @NotBlank(message = "El teléfono es obligatorio")
        String telefono,

        @NotBlank(message = "La dirección web es obligatoria")
        String direccionWeb
) {}
