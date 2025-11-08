package com.dbd.service.pagos_tarjetas.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "compras")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Compra {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String comprobanteVoucher;
    
    @Column(nullable = false)
    private String tienda;
    
    @Column(nullable = false)
    private String cuitTienda;
    
    @Column(nullable = false)
    private Double monto;
    
    @Column(nullable = false)
    private Double montoFinal;
    
    @Column(nullable = false)
    private LocalDateTime fechaHora;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tarjeta_id", nullable = false)
    private Tarjeta tarjeta;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "compra_promocion",
        joinColumns = @JoinColumn(name = "compra_id"),
        inverseJoinColumns = @JoinColumn(name = "promocion_id")
    )
    @Builder.Default
    private List<Promocion> promocionesAplicadas = new ArrayList<>();

    public abstract String getTipo();

    public abstract void calcularMontoFinal();
}
