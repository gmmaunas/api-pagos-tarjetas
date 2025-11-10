package com.dbd.service.pagos_tarjetas.repository;

import com.dbd.service.pagos_tarjetas.model.CompraCuotas;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CompraCuotasRepository extends MongoRepository<CompraCuotas, String> {

    List<CompraCuotas> findByTarjetaId(String tarjetaId);
}
