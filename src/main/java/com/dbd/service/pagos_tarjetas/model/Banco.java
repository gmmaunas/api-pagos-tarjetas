package com.dbd.service.pagos_tarjetas.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bancos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Banco {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String nombre;
    
    @Column(nullable = false, unique = true)
    private String cuit;
    
    @Column(nullable = false)
    private String direccion;
    
    @Column(nullable = false)
    private String telefono;
    
    @Column(nullable = false)
    private String direccionWeb;
    
    @ManyToMany(mappedBy = "bancos")
    @Builder.Default
    private List<TitularTarjeta> miembros = new ArrayList<>();
    
    @OneToMany(mappedBy = "banco", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Promocion> promociones = new ArrayList<>();
    
    @OneToMany(mappedBy = "banco", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Tarjeta> tarjetas = new ArrayList<>();
}
