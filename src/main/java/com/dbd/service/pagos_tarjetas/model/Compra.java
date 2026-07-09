package com.dbd.service.pagos_tarjetas.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import java.time.LocalDateTime;

@Document(collection = "compras")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Compra {

    @Id
    private String id;

    private String comprobanteVoucher;

    private String tienda;

    private String cuitTienda;

    private Double monto;

    private Double montoFinal;

    private LocalDateTime fechaHora;

    @DBRef
    private Tarjeta tarjeta;

    @DBRef
    private Promocion promocionAplicada;

    public abstract String getTipo();

    public abstract void calcularMontoFinal();
}
