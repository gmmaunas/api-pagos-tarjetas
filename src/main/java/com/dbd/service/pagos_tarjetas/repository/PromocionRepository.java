package com.dbd.service.pagos_tarjetas.repository;

import com.dbd.service.pagos_tarjetas.model.Promocion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromocionRepository extends MongoRepository<Promocion, String> {

    Optional<Promocion> findByCodigo(String codigo);

    List<Promocion> findByBancoId(String bancoId);

    // Promociones disponibles de un local entre dos fechas
    @Query("{ 'cuitTienda': ?0, 'fechaInicioValidez': { $lte: ?2 }, 'fechaFinValidez': { $gte: ?1 } }")
    List<Promocion> findPromocionesDisponiblesPorLocalYFechas(
            String cuitTienda,
            LocalDate fechaInicio,
            LocalDate fechaFin
    );
}
