package com.dbd.service.pagos_tarjetas.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import java.time.LocalDate;

@Document(collection = "promociones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class Promocion {

    @Id
    private String id;

    @Indexed(unique = true)
    private String codigo;

    private String tituloPromocion;

    private String nombreTienda;

    private String cuitTienda;

    private LocalDate fechaInicioValidez;

    private LocalDate fechaFinValidez;

    private String comentarios;

    @DBRef
    private Banco banco;

    // Campo discriminador para identificar el tipo
    private String tipo;

    public boolean esValida(LocalDate fecha) {
        return !fecha.isBefore(fechaInicioValidez) && !fecha.isAfter(fechaFinValidez);
    }

    public abstract String getTipo();

    public abstract Double aplicarAPagoUnico(CompraPagoUnico compra, Double montoActual);

    public abstract Double aplicarACuotas(CompraCuotas compra);
}
