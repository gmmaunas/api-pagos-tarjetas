package com.dbd.service.pagos_tarjetas.repository;

import com.dbd.service.pagos_tarjetas.model.CompraPagoUnico;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CompraPagoUnicoRepository extends MongoRepository<CompraPagoUnico, String> {

    List<CompraPagoUnico> findByPagoIsNull();

    List<CompraPagoUnico> findByPagoId(String pagoId);

    // Las queries complejas se movieron al servicio
}
