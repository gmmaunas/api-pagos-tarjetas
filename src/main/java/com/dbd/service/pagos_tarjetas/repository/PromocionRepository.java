package com.dbd.service.pagos_tarjetas.repository;

import com.dbd.service.pagos_tarjetas.model.Promocion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromocionRepository extends JpaRepository<Promocion, Long> {
    
    Optional<Promocion> findByCodigo(String codigo);

    // Obtener promoción por código con banco
    @Query("SELECT p FROM Promocion p JOIN FETCH p.banco WHERE p.codigo = :codigo")
    Optional<Promocion> findByCodigoConBanco(@Param("codigo") String codigo);

    List<Promocion> findByBancoId(Long bancoId);
    
    // Obtener promociones disponibles de un local entre dos fechas
    @Query("SELECT p FROM Promocion p JOIN FETCH p.banco " +
            "WHERE p.cuitTienda = :cuitTienda " +
            "AND p.fechaInicioValidez <= :fechaFin " +
            "AND p.fechaFinValidez >= :fechaInicio")
    List<Promocion> findPromocionesDisponiblesPorLocalYFechas(
        @Param("cuitTienda") String cuitTienda,
        @Param("fechaInicio") LocalDate fechaInicio,
        @Param("fechaFin") LocalDate fechaFin
    );
    
    // Obtener promociones válidas para una fecha específica
    @Query("SELECT p FROM Promocion p JOIN FETCH p.banco " +
            "WHERE p.cuitTienda = :cuitTienda " +
            "AND p.fechaInicioValidez <= :fecha " +
            "AND p.fechaFinValidez >= :fecha")
    List<Promocion> findPromocionesValidasPorTiendaYFecha(
        @Param("cuitTienda") String cuitTienda,
        @Param("fecha") LocalDate fecha
    );
}
