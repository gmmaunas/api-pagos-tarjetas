package com.dbd.service.pagos_tarjetas.repository;

import com.dbd.service.pagos_tarjetas.model.Descuento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface DescuentoRepository extends JpaRepository<Descuento, Long> {
    
    List<Descuento> findByBancoId(Long bancoId);
    
    List<Descuento> findBySoloContado(Boolean soloContado);
    
    // Obtener descuentos válidos para una tienda en una fecha
    @Query("SELECT d FROM Descuento d " +
           "WHERE d.cuitTienda = :cuitTienda " +
           "AND d.fechaInicioValidez <= :fecha " +
           "AND d.fechaFinValidez >= :fecha")
    List<Descuento> findDescuentosValidosPorTiendaYFecha(
        @Param("cuitTienda") String cuitTienda,
        @Param("fecha") LocalDate fecha
    );
}
