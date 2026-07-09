package com.dbd.service.pagos_tarjetas.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Financiacion extends Promocion {

    private Integer numeroCuotas;

    private Double interes;

    @Override
    public String getTipo() {
        return "FINANCIACION";
    }

    @Override
    public Double aplicarAPagoUnico(CompraPagoUnico compra, Double montoActual) {
        return 0.0; // No aplica descuento a pago único
    }

    @Override
    public Double aplicarACuotas(CompraCuotas compra) {
        return calcularInteresParaCuotas(compra);
    }

    @Override
    public Double calcularInteresParaCuotas(CompraCuotas compra) {
        if (!compra.getNumeroCuotas().equals(this.numeroCuotas)) {
            return null;
        }
        return this.interes;
    }

    @Override
    public Double calcularDescuentoParaCuotas(CompraCuotas compra) {
        return 0.0;
    }
}
