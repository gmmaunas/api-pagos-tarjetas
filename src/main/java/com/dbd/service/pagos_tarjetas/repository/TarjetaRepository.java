package com.dbd.service.pagos_tarjetas.repository;

import com.dbd.service.pagos_tarjetas.model.Tarjeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TarjetaRepository extends JpaRepository<Tarjeta, Long> {
    
    Optional<Tarjeta> findByNumero(String numero);
    
    List<Tarjeta> findByTitularTarjetaId(Long titularId);
    
    List<Tarjeta> findByBancoId(Long bancoId);

    // Obtener todas las tarjetas con titular y banco cargados
    @Query("SELECT t FROM Tarjeta t JOIN FETCH t.titularTarjeta JOIN FETCH t.banco")
    List<Tarjeta> findAllWithTitularAndBanco();

    // Obtener el listado de tarjetas emitidas hace más de N años
    @Query("SELECT t FROM Tarjeta t JOIN FETCH t.titularTarjeta JOIN FETCH t.banco WHERE t.desde < :fechaLimite")
    List<Tarjeta> findTarjetasEmitidasHaceMasDeNAnios(@Param("fechaLimite") LocalDate fechaLimite);
}
