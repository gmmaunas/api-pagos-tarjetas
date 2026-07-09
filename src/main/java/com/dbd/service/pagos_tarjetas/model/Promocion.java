package com.dbd.service.pagos_tarjetas.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "promociones")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Promocion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String codigo;
    
    @Column(nullable = false)
    private String tituloPromocion;
    
    @Column(nullable = false)
    private String nombreTienda;
    
    @Column(nullable = false)
    private String cuitTienda;
    
    @Column(nullable = false)
    private LocalDate fechaInicioValidez;
    
    @Column(nullable = false)
    private LocalDate fechaFinValidez;
    
    private String comentarios;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banco_id", nullable = false)
    private Banco banco;
    
    @OneToMany(mappedBy = "promocionAplicada", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Compra> compras = new ArrayList<>();
    
    public boolean esValida(LocalDate fecha) {
        return !fecha.isBefore(fechaInicioValidez) && !fecha.isAfter(fechaFinValidez);
    }

    public abstract String getTipo();

    // Devuelve el descuento aplicado al pago único (0.0 si no aplica)
    public abstract Double aplicarAPagoUnico(CompraPagoUnico compra, Double montoActual);

    // Devuelve el interés de reemplazo para cuotas (null = no reemplaza el interés base)
    public abstract Double calcularInteresParaCuotas(CompraCuotas compra);

    // Devuelve el importe de descuento a restar en cuotas (0.0 si no aplica)
    public abstract Double calcularDescuentoParaCuotas(CompraCuotas compra);

    // Indica si esta promoción es candidata para una compra en pago único
    public abstract boolean seAplicaAPagoUnico();

    // Indica si esta promoción es la financiación preferida para el número de cuotas dado
    public abstract boolean seAplicaACuotasConNumero(int numeroCuotas);

    // Indica si esta promoción aplica como descuento genérico a cuotas
    public abstract boolean seAplicaACuotasComoDescuento();
}
