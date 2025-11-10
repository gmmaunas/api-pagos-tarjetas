package com.dbd.service.pagos_tarjetas.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "pagos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pago {

    @Id
    private String id;

    @Indexed(unique = true)
    private String codigo;

    private String mes;

    private String anio;

    private LocalDate primerVencimiento;

    private LocalDate segundoVencimiento;

    private Double recargoPrimerVencimiento;

    private Double recargoSegundoVencimiento;

    private Double precioTotal;

    // Cuotas embebidas (copiadas desde las compras)
    @Builder.Default
    private List<Cuota> cuotas = new ArrayList<>();

    // IDs de compras en pago único (referencias)
    @Builder.Default
    private List<String> comprasPagoUnicoIds = new ArrayList<>();

    // Campo transient para cargar las compras cuando sea necesario
    @Transient
    private List<CompraPagoUnico> comprasPagoUnico;

    public void calcularPrecioTotal() {
        Double total = 0.0;

        for (Cuota cuota : cuotas) {
            total += cuota.getPrecio();
        }

        // Las compras de pago único se deben buscar por sus IDs y sumar sus montos
        // Esto se hace en el servicio

        this.precioTotal = total;
    }
}
