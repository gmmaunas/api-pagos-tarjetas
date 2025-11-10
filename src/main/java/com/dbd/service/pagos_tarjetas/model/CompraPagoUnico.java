package com.dbd.service.pagos_tarjetas.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CompraPagoUnico extends Compra {

    private Double descuentoTienda;

    @DBRef
    private Pago pago;

    @Override
    public String getTipo() {
        return "PAGO_UNICO";
    }

    @Override
    public void calcularMontoFinal() {
        // Aplicar descuento de la tienda primero
        Double montoConDescuentoTienda = getMonto();
        if (descuentoTienda != null && descuentoTienda > 0) {
            montoConDescuentoTienda = getMonto() * (1 - descuentoTienda / 100);
        }

        // Aplicar promociones del banco
        Double descuentoTotal = 0.0;
        for (Promocion promo : getPromocionesAplicadas()) {
            descuentoTotal += promo.aplicarAPagoUnico(this, montoConDescuentoTienda);
        }

        setMontoFinal(montoConDescuentoTienda - descuentoTotal);
    }
}
