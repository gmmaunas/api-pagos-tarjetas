package com.dbd.service.pagos_tarjetas.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "bancos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Banco {

    @Id
    private String id;

    private String nombre;

    @Indexed(unique = true)
    private String cuit;

    private String direccion;

    private String telefono;

    private String direccionWeb;
}
