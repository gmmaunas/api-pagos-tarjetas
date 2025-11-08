package com.dbd.service.pagos_tarjetas.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pagos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pago {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String codigo;
    
    @Column(nullable = false)
    private String mes;
    
    @Column(nullable = false)
    private String anio;
    
    @Column(nullable = false)
    private LocalDate primerVencimiento;
    
    @Column(nullable = false)
    private LocalDate segundoVencimiento;
    
    @Column(nullable = false)
    private Double recargoPrimerVencimiento;
    
    @Column(nullable = false)
    private Double recargoSegundoVencimiento;
    
    @Column(nullable = false)
    private Double precioTotal;
    
    @OneToMany(mappedBy = "pago", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Cuota> cuotas = new ArrayList<>();
    
    @OneToMany(mappedBy = "pago", cascade = CascadeType.ALL)
    @Builder.Default
    private List<CompraPagoUnico> comprasPagoUnico = new ArrayList<>();
    
    public void calcularPrecioTotal() {
        Double total = 0.0;
        
        for (Cuota cuota : cuotas) {
            total += cuota.getPrecio();
        }
        
        for (CompraPagoUnico compra : comprasPagoUnico) {
            total += compra.getMontoFinal();
        }
        
        this.precioTotal = total;
    }
}
