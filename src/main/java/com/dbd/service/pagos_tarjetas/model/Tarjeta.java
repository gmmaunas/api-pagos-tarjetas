package com.dbd.service.pagos_tarjetas.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import java.time.LocalDate;

@Document(collection = "tarjetas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tarjeta {

    @Id
    private String id;

    @Indexed(unique = true)
    private String numero;

    private String ccv;

    private String nombreTitularTarjeta;

    private LocalDate desde;

    private LocalDate fechaVencimiento;

    @DBRef
    private TitularTarjeta titularTarjeta;

    @DBRef
    private Banco banco;
}
