package com.dbd.service.pagos_tarjetas.repository;

import com.dbd.service.pagos_tarjetas.model.CompraCuotas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompraCuotasRepository extends JpaRepository<CompraCuotas, Long> {
    
    List<CompraCuotas> findByTarjetaId(Long tarjetaId);
    
    // Obtener compra en cuotas con todas sus cuotas cargadas
    @Query("SELECT c FROM CompraCuotas c " +
           "LEFT JOIN FETCH c.cuotas " +
           "WHERE c.id = :compraId")
    Optional<CompraCuotas> findByIdConCuotas(@Param("compraId") Long compraId);
}
