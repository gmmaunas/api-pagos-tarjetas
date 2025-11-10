package com.dbd.service.pagos_tarjetas.repository;

import com.dbd.service.pagos_tarjetas.model.Financiacion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FinanciacionRepository extends MongoRepository<Financiacion, String> {

    List<Financiacion> findByBancoId(String bancoId);

    List<Financiacion> findByNumeroCuotas(Integer numeroCuotas);
}
