package com.dbd.service.pagos_tarjetas.service;

import com.dbd.service.pagos_tarjetas.model.Tarjeta;
import java.util.List;

/**
 * Interfaz del servicio para la gestión de Tarjetas
 */
public interface ITarjetaService {

    /**
     * Crea una nueva tarjeta
     * @param tarjeta Tarjeta a crear
     * @return Tarjeta creada
     */
    Tarjeta crearTarjeta(Tarjeta tarjeta);

    /**
     * Obtiene una tarjeta por su ID
     * @param id ID de la tarjeta
     * @return Tarjeta encontrada
     */
    Tarjeta obtenerTarjetaPorId(Long id);

    /**
     * Obtiene una tarjeta por su número
     * @param numero Número de la tarjeta
     * @return Tarjeta encontrada
     */
    Tarjeta obtenerTarjetaPorNumero(String numero);

    /**
     * Obtiene todas las tarjetas
     * @return Lista de tarjetas
     */
    List<Tarjeta> obtenerTodasLasTarjetas();

    /**
     * Obtiene las tarjetas de un titular específico
     * @param titularId ID del titular
     * @return Lista de tarjetas del titular
     */
    List<Tarjeta> obtenerTarjetasPorTitular(Long titularId);

    /**
     * Obtiene las tarjetas de un banco específico
     * @param bancoId ID del banco
     * @return Lista de tarjetas del banco
     */
    List<Tarjeta> obtenerTarjetasPorBanco(Long bancoId);

    /**
     * Actualiza una tarjeta existente
     * @param id ID de la tarjeta a actualizar
     * @param tarjetaActualizada Datos actualizados de la tarjeta
     * @return Tarjeta actualizada
     */
    Tarjeta actualizarTarjeta(Long id, Tarjeta tarjetaActualizada);

    /**
     * Elimina una tarjeta por su ID
     * @param id ID de la tarjeta a eliminar
     */
    void eliminarTarjeta(Long id);

    /**
     * Obtiene el listado de tarjetas emitidas hace más de N años
     * @param anios Cantidad de años
     * @return Lista de tarjetas antiguas
     */
    List<Tarjeta> obtenerTarjetasEmitidasHaceMasDeNAnios(int anios);
}
