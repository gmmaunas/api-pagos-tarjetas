package com.dbd.service.pagos_tarjetas.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "titulares_tarjeta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TitularTarjeta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombreCompleto;
    
    @Column(nullable = false)
    private String dni;
    
    @Column(nullable = false, unique = true)
    private String cuit;
    
    @Column(nullable = false)
    private String direccion;
    
    @Column(nullable = false)
    private String telefono;
    
    @Column(nullable = false)
    private LocalDate fechaAlta;
    
    @ManyToMany
    @JoinTable(
        name = "banco_titular",
        joinColumns = @JoinColumn(name = "titular_id"),
        inverseJoinColumns = @JoinColumn(name = "banco_id")
    )
    @Builder.Default
    private List<Banco> bancos = new ArrayList<>();

    @OneToMany(mappedBy = "titularTarjeta", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Tarjeta> tarjetas = new ArrayList<>();
}
