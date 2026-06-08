package com.dbd.service.pagos_tarjetas.repository;

import com.dbd.service.pagos_tarjetas.model.TitularTarjeta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TitularTarjetaRepository extends MongoRepository<TitularTarjeta, String> {

    Optional<TitularTarjeta> findByCuit(String cuit);

    Optional<TitularTarjeta> findByDni(String dni);

    // Las queries complejas se movieron al servicio
}
