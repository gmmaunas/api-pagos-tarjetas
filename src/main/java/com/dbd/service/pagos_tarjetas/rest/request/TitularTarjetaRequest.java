package com.dbd.service.pagos_tarjetas.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

public record TitularTarjetaRequest(
        @NotBlank(message = "El nombre completo es obligatorio")
        String nombreCompleto,

        @NotBlank(message = "El DNI es obligatorio")
        String dni,

        @NotBlank(message = "El CUIT es obligatorio")
        String cuit,

        @NotBlank(message = "La dirección es obligatoria")
        String direccion,

        @NotBlank(message = "El teléfono es obligatorio")
        String telefono,

        @NotNull(message = "La fecha de alta es obligatoria")
        LocalDate fechaAlta,

        @NotNull(message = "Los IDs de los bancos son obligatorios")
        @Size(min = 1, message = "Debe especificar al menos un banco")
        List<Long> bancoIds
) {}
