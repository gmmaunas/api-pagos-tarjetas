package com.dbd.service.pagos_tarjetas.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "compras_pago_unico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CompraPagoUnico extends Compra {
    
    @Column(nullable = false)
    @Builder.Default
    private Double descuentoTienda = 0.0;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pago_id")
    private Pago pago;

    @Override
    public String getTipo() {
        return "PAGO_UNICO";
    }

    @Override
    public void calcularMontoFinal() {
        Double montoConDescuentoTienda = getMonto() * (1 - descuentoTienda / 100);
        Double descuentoPromocion = 0.0;

        Promocion promo = getPromocionAplicada();
        if (promo != null) {
            descuentoPromocion = promo.aplicarAPagoUnico(this, montoConDescuentoTienda);
        }

        setMontoFinal(montoConDescuentoTienda - descuentoPromocion);
    }
}
