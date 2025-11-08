package com.dbd.service.pagos_tarjetas.repository;

import com.dbd.service.pagos_tarjetas.model.CompraPagoUnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CompraPagoUnicoRepository extends JpaRepository<CompraPagoUnico, Long> {
    
    List<CompraPagoUnico> findByPagoIsNull();
    
    List<CompraPagoUnico> findByPagoId(Long pagoId);
    
    // Obtener compras en un solo pago de un mes específico sin asignar a pago
    @Query("SELECT c FROM CompraPagoUnico c " +
            "JOIN FETCH c.tarjeta " +
            "WHERE FUNCTION('MONTH', c.fechaHora) = :mes " +
            "AND FUNCTION('YEAR', c.fechaHora) = :anio " +
            "AND c.pago IS NULL")
    List<CompraPagoUnico> findComprasSinPagoPorMesAnio(@Param("mes") int mes, @Param("anio") int anio);
}
