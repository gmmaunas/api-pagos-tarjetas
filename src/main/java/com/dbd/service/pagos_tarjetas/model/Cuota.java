package com.dbd.service.pagos_tarjetas.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cuota {

    private Integer numero;

    private Double precio;

    private String mes;

    private String anio;

    // Referencia al pago (cuando la cuota es asignada a un pago)
    private String pagoId;

    // Referencia a la compra origen de la cuota
    private String compraId;
}
