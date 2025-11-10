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
        // Interés base de la compra
        Double interesAplicado = interes;
        Double descuentoTotal = 0.0;

        // Aplicar promociones usando Double Dispatch
        for (Promocion promo : getPromocionesAplicadas()) {
            // Cada promoción sabe cómo aplicarse a cuotas

            // Si es financiación, puede reemplazar el interés
            Double interesPromocion = promo.aplicarACuotas(this);
            if (interesPromocion != null) {
                interesAplicado = interesPromocion;
                break; // Solo una financiación aplica
            }

            // Si es descuento, acumular
            descuentoTotal += (interesPromocion != null ? 0.0 : promo.aplicarACuotas(this));
        }

        // Calcular monto final con interés
        Double montoConInteres = getMonto() * (1 + interesAplicado / 100);

        setMontoFinal(montoConInteres - descuentoTotal);
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
