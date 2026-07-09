package com.dbd.service.pagos_tarjetas.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CompraCuotas extends Compra {

    private Integer numeroCuotas;

    private Double interes;

    // Cuotas embebidas
    @Builder.Default
    private List<Cuota> cuotas = new ArrayList<>();

    @Override
    public String getTipo() {
        return "CUOTAS";
    }

    @Override
    public void calcularMontoFinal() {
        Double interesAplicado = interes;
        Double descuentoTotal = 0.0;

        Promocion promo = getPromocionAplicada();
        if (promo != null) {
            Double interesPromocion = promo.calcularInteresParaCuotas(this);
            if (interesPromocion != null) {
                interesAplicado = interesPromocion;
            }
            descuentoTotal = promo.calcularDescuentoParaCuotas(this);
        }

        setMontoFinal(getMonto() * (1 + interesAplicado / 100) - descuentoTotal);
    }

    public void generarCuotas() {
        cuotas.clear();
        Double montoCuota = getMontoFinal() / numeroCuotas;

        for (int i = 1; i <= numeroCuotas; i++) {
            Cuota cuota = Cuota.builder()
                    .numero(i)
                    .precio(montoCuota)
                    .mes(String.format("%02d", (getFechaHora().getMonthValue() + i - 1) % 12 + 1))
                    .anio(String.valueOf(getFechaHora().getYear() + (getFechaHora().getMonthValue() + i - 1) / 12))
                    .build();
            cuotas.add(cuota);
        }
    }
}
