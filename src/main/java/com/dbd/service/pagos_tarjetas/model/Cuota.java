package com.dbd.service.pagos_tarjetas.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cuotas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cuota {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Integer numero;
    
    @Column(nullable = false)
    private Double precio;
    
    @Column(nullable = false)
    private String mes;
    
    @Column(nullable = false)
    private String anio;
    
    @Column(name = "compra_id", insertable = false, updatable = false)
    private Long compraId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compra_id", nullable = false)
    private CompraCuotas compra;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pago_id")
    private Pago pago;
}
