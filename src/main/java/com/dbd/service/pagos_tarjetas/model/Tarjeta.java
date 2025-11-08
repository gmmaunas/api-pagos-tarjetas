package com.dbd.service.pagos_tarjetas.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tarjetas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tarjeta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String numero;
    
    @Column(nullable = false)
    private String ccv;
    
    @Column(nullable = false)
    private String nombreTitularTarjeta;
    
    @Column(nullable = false)
    private LocalDate desde;
    
    @Column(nullable = false)
    private LocalDate fechaVencimiento;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "titular_id", nullable = false)
    private TitularTarjeta titularTarjeta;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banco_id", nullable = false)
    private Banco banco;
    
    @OneToMany(mappedBy = "tarjeta", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Compra> compras = new ArrayList<>();
}
