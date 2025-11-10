package com.dbd.service.pagos_tarjetas.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import java.time.LocalDate;

@Document(collection = "titulares_tarjeta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TitularTarjeta {

    @Id
    private String id;

    private String nombreCompleto;

    private String dni;

    @Indexed(unique = true)
    private String cuit;

    private String direccion;

    private String telefono;

    private LocalDate fechaAlta;

    @DBRef
    private Banco banco;
}
