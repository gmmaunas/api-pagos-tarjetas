package com.dbd.service.pagos_tarjetas.service;

import com.dbd.service.pagos_tarjetas.model.Descuento;
import com.dbd.service.pagos_tarjetas.model.Financiacion;
import com.dbd.service.pagos_tarjetas.model.Promocion;
import java.time.LocalDate;
import java.util.List;

/**
 * Interfaz del servicio para la gestión de Promociones
 */
public interface IPromocionService {

    /**
     * Agrega una nueva promoción de tipo descuento a un banco
     * @param descuento Descuento a agregar
     * @return Descuento creado
     */
    Descuento agregarDescuento(Descuento descuento);

    /**
     * Agrega una nueva promoción de tipo financiación
     * @param financiacion Financiación a agregar
     * @return Financiación creada
     */
    Financiacion agregarFinanciacion(Financiacion financiacion);

    /**
     * Obtiene una promoción por su ID
     * @param id ID de la promoción
     * @return Promoción encontrada
     */
    Promocion obtenerPromocionPorId(Long id);

    /**
     * Obtiene una promoción por su código
     * @param codigo Código de la promoción
     * @return Promoción encontrada
     */
    Promocion obtenerPromocionPorCodigo(String codigo);

    /**
     * Obtiene todas las promociones
     * @return Lista de promociones
     */
    List<Promocion> obtenerTodasLasPromociones();

    /**
     * Obtiene las promociones de un banco específico
     * @param bancoId ID del banco
     * @return Lista de promociones del banco
     */
    List<Promocion> obtenerPromocionesPorBanco(Long bancoId);

    /**
     * Obtiene el listado de las promociones disponibles de un local entre dos fechas
     * @param cuitTienda CUIT de la tienda
     * @param fechaInicio Fecha de inicio del rango
     * @param fechaFin Fecha de fin del rango
     * @return Lista de promociones disponibles
     */
    List<Promocion> obtenerPromocionesDisponiblesPorLocalYFechas(
            String cuitTienda,
            LocalDate fechaInicio,
            LocalDate fechaFin);

    /**
     * Elimina una promoción a través de su código
     * @param codigo Código de la promoción a eliminar
     */
    void eliminarPromocionPorCodigo(String codigo);

    /**
     * Elimina una promoción por su ID
     * @param id ID de la promoción a eliminar
     */
    void eliminarPromocion(Long id);

    /**
     * Actualiza una promoción existente
     * @param id ID de la promoción a actualizar
     * @param promocionActualizada Datos actualizados de la promoción
     * @return Promoción actualizada
     */
    Promocion actualizarPromocion(Long id, Promocion promocionActualizada);
}
