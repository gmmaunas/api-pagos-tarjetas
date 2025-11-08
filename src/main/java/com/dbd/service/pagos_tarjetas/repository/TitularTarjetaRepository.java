package com.dbd.service.pagos_tarjetas.repository;

import com.dbd.service.pagos_tarjetas.model.TitularTarjeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TitularTarjetaRepository extends JpaRepository<TitularTarjeta, Long> {
    
    Optional<TitularTarjeta> findByCuit(String cuit);
    
    Optional<TitularTarjeta> findByDni(String dni);
    
    List<TitularTarjeta> findByBancoId(Long bancoId);

    // Obtener todos los titulares con su banco cargado
    @Query("SELECT t FROM TitularTarjeta t JOIN FETCH t.banco")
    List<TitularTarjeta> findAllWithBanco();

    // Obtener los nombres de los N titulares con mayor monto total en compras
    @Query("SELECT t.nombreCompleto, SUM(c.montoFinal) as total " +
            "FROM TitularTarjeta t " +
            "JOIN t.tarjetas tar " +
            "JOIN tar.compras c " +
            "GROUP BY t.id, t.nombreCompleto " +
            "ORDER BY total DESC")
    List<Object[]> findTopNTitularesConMayorMontoCompras(@Param("limit") int limit);
}
