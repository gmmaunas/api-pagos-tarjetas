package com.dbd.service.pagos_tarjetas.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "compras_cuotas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CompraCuotas extends Compra {
    
    @Column(nullable = false)
    @Builder.Default
    private Double interes = 0.0;
    
    @Column(nullable = false)
    private Integer numeroCuotas;
    
    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
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
                    .compra(this)
                    .build();
            cuotas.add(cuota);
        }
    }
}
