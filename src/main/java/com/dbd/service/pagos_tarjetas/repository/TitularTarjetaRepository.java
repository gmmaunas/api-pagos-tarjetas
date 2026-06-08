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

    // Obtener titular por id con bancos cargados
    @Query("SELECT t FROM TitularTarjeta t LEFT JOIN FETCH t.bancos WHERE t.id = :id")
    Optional<TitularTarjeta> findByIdWithBancos(@Param("id") Long id);

    // Obtener titular por cuit con bancos cargados
    @Query("SELECT t FROM TitularTarjeta t LEFT JOIN FETCH t.bancos WHERE t.cuit = :cuit")
    Optional<TitularTarjeta> findByCuitWithBancos(@Param("cuit") String cuit);

    // Obtener titular por dni con bancos cargados
    @Query("SELECT t FROM TitularTarjeta t LEFT JOIN FETCH t.bancos WHERE t.dni = :dni")
    Optional<TitularTarjeta> findByDniWithBancos(@Param("dni") String dni);

    // Obtener titulares de un banco (usando la relación ManyToMany)
    @Query("SELECT DISTINCT t FROM TitularTarjeta t LEFT JOIN FETCH t.bancos b WHERE b.id = :bancoId")
    List<TitularTarjeta> findByBancoId(@Param("bancoId") Long bancoId);

    // Obtener todos los titulares con sus bancos cargados
    @Query("SELECT DISTINCT t FROM TitularTarjeta t LEFT JOIN FETCH t.bancos")
    List<TitularTarjeta> findAllWithBancos();

    // Obtener los nombres de los N titulares con mayor monto total en compras
    @Query("SELECT t.nombreCompleto, SUM(c.montoFinal) as total " +
            "FROM TitularTarjeta t " +
            "JOIN t.tarjetas tar " +
            "JOIN tar.compras c " +
            "GROUP BY t.id, t.nombreCompleto " +
            "ORDER BY total DESC")
    List<Object[]> findTopNTitularesConMayorMontoCompras(@Param("limit") int limit);
}
