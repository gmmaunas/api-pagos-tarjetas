package com.dbd.service.pagos_tarjetas.repository;

import com.dbd.service.pagos_tarjetas.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface CompraRepository extends JpaRepository<Compra, Long> {
    
    Optional<Compra> findByComprobanteVoucher(String comprobanteVoucher);
    
    List<Compra> findByTarjetaId(Long tarjetaId);
    
    List<Compra> findByCuitTienda(String cuitTienda);
    
    // Obtener información de una compra con sus detalles
    @Query("SELECT c FROM Compra c " +
            "LEFT JOIN FETCH c.tarjeta t " +
            "LEFT JOIN FETCH c.promocionAplicada p " +
            "LEFT JOIN FETCH p.banco " +
            "WHERE c.id = :compraId")
    Optional<Compra> findByIdConDetalles(@Param("compraId") Long compraId);

    // Cargar todas las compras con tarjeta y promoción para evitar lazy fuera de tx
    @Query("SELECT DISTINCT c FROM Compra c LEFT JOIN FETCH c.tarjeta LEFT JOIN FETCH c.promocionAplicada vp LEFT JOIN FETCH vp.banco")
    List<Compra> findAllConDetalles();

    // Cargar compras de una tarjeta con sus relaciones
    @Query("SELECT DISTINCT c FROM Compra c LEFT JOIN FETCH c.tarjeta LEFT JOIN FETCH c.promocionAplicada vp LEFT JOIN FETCH vp.banco WHERE c.tarjeta.id = :tarjetaId")
    List<Compra> findByTarjetaIdConDetalles(@Param("tarjetaId") Long tarjetaId);

    // Obtener el nombre del local con mayor cantidad de compras
    @Query("SELECT c.tienda, COUNT(c.id) as total " +
           "FROM Compra c " +
           "GROUP BY c.tienda, c.cuitTienda " +
           "ORDER BY total DESC")
    List<Object[]> findTiendaConMasCompras();
}
