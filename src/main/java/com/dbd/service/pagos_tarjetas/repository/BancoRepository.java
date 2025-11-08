package com.dbd.service.pagos_tarjetas.repository;

import com.dbd.service.pagos_tarjetas.model.Banco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BancoRepository extends JpaRepository<Banco, Long> {
    
    Optional<Banco> findByCuit(String cuit);
    
    // Obtener el banco con mayor cantidad de compras realizadas con sus tarjetas
    @Query("SELECT b FROM Banco b " +
            "LEFT JOIN b.tarjetas t " +
            "LEFT JOIN t.compras c " +
            "GROUP BY b.id " +
            "ORDER BY COUNT(c.id) DESC")
    List<Banco> findBancoConMasCompras();
    
    // Obtener el número de clientes de cada banco
    @Query("SELECT b.nombre, COUNT(DISTINCT t.id) " +
           "FROM Banco b " +
           "LEFT JOIN b.miembros t " +
           "GROUP BY b.id, b.nombre")
    java.util.List<Object[]> contarClientesPorBanco();
}
