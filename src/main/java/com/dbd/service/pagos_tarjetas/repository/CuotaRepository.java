package com.dbd.service.pagos_tarjetas.repository;

import com.dbd.service.pagos_tarjetas.model.Cuota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CuotaRepository extends JpaRepository<Cuota, Long> {
    
    List<Cuota> findByCompraId(Long compraId);
    
    List<Cuota> findByPagoId(Long pagoId);
    
    List<Cuota> findByPagoIsNull();
    
    // Obtener cuotas de un mes y año específico sin asignar a pago
    @Query("SELECT c FROM Cuota c " +
           "WHERE c.mes = :mes " +
           "AND c.anio = :anio " +
           "AND c.pago IS NULL")
    List<Cuota> findCuotasSinPagoPorMesAnio(@Param("mes") String mes, @Param("anio") String anio);
}
