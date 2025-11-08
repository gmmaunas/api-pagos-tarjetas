package com.dbd.service.pagos_tarjetas.repository;

import com.dbd.service.pagos_tarjetas.model.Financiacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface FinanciacionRepository extends JpaRepository<Financiacion, Long> {
    
    List<Financiacion> findByBancoId(Long bancoId);
    
    List<Financiacion> findByNumeroCuotas(Integer numeroCuotas);
    
    // Obtener financiaciones válidas para una tienda, fecha y número de cuotas
    @Query("SELECT f FROM Financiacion f " +
           "WHERE f.cuitTienda = :cuitTienda " +
           "AND f.numeroCuotas = :numeroCuotas " +
           "AND f.fechaInicioValidez <= :fecha " +
           "AND f.fechaFinValidez >= :fecha")
    List<Financiacion> findFinanciacionesValidasPorTiendaFechaYCuotas(
        @Param("cuitTienda") String cuitTienda,
        @Param("numeroCuotas") Integer numeroCuotas,
        @Param("fecha") LocalDate fecha
    );
}
