package com.dbd.service.pagos_tarjetas.repository;

import com.dbd.service.pagos_tarjetas.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
    
    Optional<Compra> findByComprobanteVoucher(String comprobanteVoucher);
    
    List<Compra> findByTarjetaId(Long tarjetaId);
    
    List<Compra> findByCuitTienda(String cuitTienda);
    
    // Obtener información de una compra con sus detalles
    @Query("SELECT c FROM Compra c " +
            "LEFT JOIN FETCH c.tarjeta t " +
            "LEFT JOIN FETCH c.promocionesAplicadas p " +
            "LEFT JOIN FETCH p.banco " +
            "WHERE c.id = :compraId")
    Optional<Compra> findByIdConDetalles(@Param("compraId") Long compraId);
    
    // Obtener el nombre del local con mayor cantidad de compras
    @Query("SELECT c.tienda, COUNT(c.id) as total " +
           "FROM Compra c " +
           "GROUP BY c.tienda, c.cuitTienda " +
           "ORDER BY total DESC")
    List<Object[]> findTiendaConMasCompras();
}
