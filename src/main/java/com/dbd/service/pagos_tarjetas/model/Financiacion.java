package com.dbd.service.pagos_tarjetas.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "financiaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Financiacion extends Promocion {
    
    @Column(nullable = false)
    private Integer numeroCuotas;
    
    @Column(nullable = false)
    private Double interes;

    @Override
    public String getTipo() {
        return "FINANCIACION";
    }

    @Override
    public Double aplicarAPagoUnico(CompraPagoUnico compra, Double montoActual) {
        return 0.0; // Una financiación no aplica descuento a pago único
    }

    @Override
    public Double calcularInteresParaCuotas(CompraCuotas compra) {
        if (!this.numeroCuotas.equals(compra.getNumeroCuotas())) {
            return null; // No corresponde al número de cuotas de la compra
        }
        return this.interes;
    }

    @Override
    public Double calcularDescuentoParaCuotas(CompraCuotas compra) {
        return 0.0; // Una financiación no aplica descuento, solo reemplaza el interés
    }

    @Override
    public boolean seAplicaAPagoUnico() {
        return false; // Una financiación no aplica a pago único
    }

    @Override
    public boolean seAplicaACuotasConNumero(int numeroCuotas) {
        return this.numeroCuotas.equals(numeroCuotas);
    }

    @Override
    public boolean seAplicaACuotasComoDescuento() {
        return false; // Una financiación no es un descuento
    }
}
