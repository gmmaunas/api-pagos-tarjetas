package com.dbd.service.pagos_tarjetas.repository;

import com.dbd.service.pagos_tarjetas.model.Descuento;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DescuentoRepository extends MongoRepository<Descuento, String> {

    List<Descuento> findByBancoId(String bancoId);

    List<Descuento> findBySoloContado(Boolean soloContado);
}
