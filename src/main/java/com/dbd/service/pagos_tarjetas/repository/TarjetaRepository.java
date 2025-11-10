package com.dbd.service.pagos_tarjetas.repository;

import com.dbd.service.pagos_tarjetas.model.Tarjeta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TarjetaRepository extends MongoRepository<Tarjeta, String> {

    Optional<Tarjeta> findByNumero(String numero);

    List<Tarjeta> findByTitularTarjetaId(String titularId);

    List<Tarjeta> findByBancoId(String bancoId);

    // Tarjetas emitidas antes de una fecha
    List<Tarjeta> findByDesdeBefore(LocalDate fechaLimite);
}
