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
    public Double calcularInteresParaCuotas(CompraCuotas compra) {
        return null; // Un descuento no reemplaza el interés base
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

    @Override
    public boolean seAplicaAPagoUnico() {
        return true;
    }

    @Override
    public boolean seAplicaACuotasConNumero(int numeroCuotas) {
        return false; // Un descuento no es financiación
    }

    @Override
    public boolean seAplicaACuotasComoDescuento() {
        return !soloContado;
    }
}
