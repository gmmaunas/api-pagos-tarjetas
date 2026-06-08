package com.dbd.service.pagos_tarjetas.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Descuento extends Promocion {

    private Double porcentajeDescuento;

    private Double tope;

    private Boolean soloContado;

    @Override
    public String getTipo() {
        return "DESCUENTO";
    }

    @Override
    public Double aplicarAPagoUnico(CompraPagoUnico compra, Double montoActual) {
        // Un pago único es siempre contado, el descuento aplica independientemente de soloContado

        // Calcular el descuento
        Double descuentoAplicado = montoActual * (porcentajeDescuento / 100);

        // Aplicar tope si existe
        if (tope != null && descuentoAplicado > tope) {
            descuentoAplicado = tope;
        }

        return descuentoAplicado;
    }

    @Override
    public Double aplicarACuotas(CompraCuotas compra) {
        // Si el descuento es solo para contado, no aplica a cuotas
        if (soloContado) {
            return 0.0;
        }

        // Calcular el descuento sobre el monto base
        Double montoBase = compra.getMonto();
        Double descuentoAplicado = montoBase * (porcentajeDescuento / 100);

        // Aplicar tope si existe
        if (tope != null && descuentoAplicado > tope) {
            descuentoAplicado = tope;
        }

        return descuentoAplicado;
    }
}
