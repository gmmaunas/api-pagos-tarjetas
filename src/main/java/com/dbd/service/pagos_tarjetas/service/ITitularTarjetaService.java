package com.dbd.service.pagos_tarjetas.service;

import com.dbd.service.pagos_tarjetas.model.TitularTarjeta;
import java.util.List;
import java.util.Map;

/**
 * Interfaz del servicio para la gestión de Titulares de Tarjetas
 */
public interface ITitularTarjetaService {

    /**
     * Crea un nuevo titular de tarjeta
     * @param titular Titular a crear
     * @return Titular creado
     */
    TitularTarjeta crearTitular(TitularTarjeta titular);

    /**
     * Obtiene un titular por su ID
     * @param id ID del titular
     * @return Titular encontrado
     */
    TitularTarjeta obtenerTitularPorId(Long id);

    /**
     * Obtiene un titular por su CUIT
     * @param cuit CUIT del titular
     * @return Titular encontrado
     */
    TitularTarjeta obtenerTitularPorCuit(String cuit);

    /**
     * Obtiene un titular por su DNI
     * @param dni DNI del titular
     * @return Titular encontrado
     */
    TitularTarjeta obtenerTitularPorDni(String dni);

    /**
     * Obtiene todos los titulares
     * @return Lista de titulares
     */
    List<TitularTarjeta> obtenerTodosLosTitulares();

    /**
     * Obtiene los titulares de un banco específico
     * @param bancoId ID del banco
     * @return Lista de titulares del banco
     */
    List<TitularTarjeta> obtenerTitularesPorBanco(Long bancoId);

    /**
     * Actualiza un titular existente
     * @param id ID del titular a actualizar
     * @param titularActualizado Datos actualizados del titular
     * @return Titular actualizado
     */
    TitularTarjeta actualizarTitular(Long id, TitularTarjeta titularActualizado);

    /**
     * Elimina un titular por su ID
     * @param id ID del titular a eliminar
     */
    void eliminarTitular(Long id);

    /**
     * Obtiene los nombres de los N titulares con mayor monto total en compras
     * @param limite Cantidad de titulares a retornar
     * @return Mapa con nombre del titular y monto total
     */
    Map<String, Double> obtenerTopNTitularesConMayorMontoCompras(int limite);
}
