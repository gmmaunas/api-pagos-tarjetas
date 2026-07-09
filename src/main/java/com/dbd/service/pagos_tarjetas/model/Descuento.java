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
        return calcularDescuentoParaCuotas(compra);
    }

    @Override
    public Double calcularInteresParaCuotas(CompraCuotas compra) {
        return null;
    }

    @Override
    public Double calcularDescuentoParaCuotas(CompraCuotas compra) {
        if (soloContado) {
            return 0.0;
        }
        Double descuentoAplicado = compra.getMonto() * (porcentajeDescuento / 100);
        if (tope != null && descuentoAplicado > tope) {
            descuentoAplicado = tope;
        }
        return descuentoAplicado;
    }
}
