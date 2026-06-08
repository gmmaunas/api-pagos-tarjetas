package com.dbd.service.pagos_tarjetas.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "descuentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Descuento extends Promocion {
    
    @Column(nullable = false)
    private Double porcentajeDescuento;
    
    private Double tope;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean soloContado = false;

    @Override
    public String getTipo() {
        return "DESCUENTO";
    }

    @Override
    public Double aplicarAPagoUnico(CompraPagoUnico compra, Double montoActual) {
        Double descuentoAplicado = montoActual * (porcentajeDescuento / 100);

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
