package com.dbd.service.pagos_tarjetas.repository;

import com.dbd.service.pagos_tarjetas.model.CompraPagoUnico;
import com.dbd.service.pagos_tarjetas.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface PagoRepository extends JpaRepository<Pago, Long> {

    Optional<Pago> findByCodigo(String codigo);

    Optional<Pago> findByMesAndAnio(String mes, String anio);

    List<Pago> findByAnio(String anio);

    // Obtener pago con cuotas
    @Query("SELECT p FROM Pago p " +
            "LEFT JOIN FETCH p.cuotas " +
            "WHERE p.id = :pagoId")
    Optional<Pago> findByIdConCuotas(@Param("pagoId") Long pagoId);

    // Obtener pago con compras
    @Query("SELECT p FROM Pago p " +
            "LEFT JOIN FETCH p.comprasPagoUnico " +
            "WHERE p.id = :pagoId")
    Optional<Pago> findByIdConCompras(@Param("pagoId") Long pagoId);

    // Obtener pago por código con cuotas
    @Query("SELECT p FROM Pago p " +
            "LEFT JOIN FETCH p.cuotas " +
            "WHERE p.codigo = :codigo")
    Optional<Pago> findByCodigoConCuotas(@Param("codigo") String codigo);

    // Obtener pago por código con compras
    @Query("SELECT p FROM Pago p " +
            "LEFT JOIN FETCH p.comprasPagoUnico cpu " +
            "LEFT JOIN FETCH cpu.tarjeta " +
            "WHERE p.codigo = :codigo")
    Optional<Pago> findByCodigoConCompras(@Param("codigo") String codigo);

    // Cargar promociones de las compras de un pago
    @Query("SELECT c FROM CompraPagoUnico c " +
            "LEFT JOIN FETCH c.promocionAplicada p " +
            "LEFT JOIN FETCH p.banco " +
            "WHERE c.pago.codigo = :codigo")
    List<CompraPagoUnico> findComprasConPromocionesDelPago(@Param("codigo") String codigo);

    // Obtener todos los pagos con cuotas
    @Query("SELECT DISTINCT p FROM Pago p LEFT JOIN FETCH p.cuotas")
    List<Pago> findAllConCuotas();

    // Obtener todos los pagos con compras
    @Query("SELECT DISTINCT p FROM Pago p " +
            "LEFT JOIN FETCH p.comprasPagoUnico cpu " +
            "LEFT JOIN FETCH cpu.tarjeta")
    List<Pago> findAllConCompras();

    // Cargar promociones de todas las compras
    @Query("SELECT c FROM CompraPagoUnico c " +
            "LEFT JOIN FETCH c.promocionAplicada p " +
            "LEFT JOIN FETCH p.banco")
    List<CompraPagoUnico> findAllComprasConPromociones();
}
