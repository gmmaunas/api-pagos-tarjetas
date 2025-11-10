package com.dbd.service.pagos_tarjetas.repository;

import com.dbd.service.pagos_tarjetas.model.Banco;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface BancoRepository extends MongoRepository<Banco, String> {

    Optional<Banco> findByCuit(String cuit);

    // Las queries complejas se movieron al servicio usando MongoTemplate o Aggregation
}
