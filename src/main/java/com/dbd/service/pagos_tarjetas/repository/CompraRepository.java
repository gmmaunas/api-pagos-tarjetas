package com.dbd.service.pagos_tarjetas.repository;

import com.dbd.service.pagos_tarjetas.model.Compra;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompraRepository extends MongoRepository<Compra, String> {

    Optional<Compra> findByComprobanteVoucher(String comprobanteVoucher);

    List<Compra> findByTarjetaId(String tarjetaId);

    List<Compra> findByCuitTienda(String cuitTienda);

    // Las queries complejas se movieron al servicio
}
