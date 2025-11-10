package com.dbd.service.pagos_tarjetas.repository;

import com.dbd.service.pagos_tarjetas.model.Pago;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PagoRepository extends MongoRepository<Pago, String> {

    Optional<Pago> findByCodigo(String codigo);

    Optional<Pago> findByMesAndAnio(String mes, String anio);

    List<Pago> findByAnio(String anio);
}
