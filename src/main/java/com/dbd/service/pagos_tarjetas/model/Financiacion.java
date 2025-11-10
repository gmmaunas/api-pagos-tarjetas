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
        // Solo aplica si el número de cuotas coincide
        if (!compra.getNumeroCuotas().equals(this.numeroCuotas)) {
            return null; // No aplica esta financiación
        }

        // Retorna el interés de la financiación para reemplazar el interés base
        return this.interes;
    }
}
