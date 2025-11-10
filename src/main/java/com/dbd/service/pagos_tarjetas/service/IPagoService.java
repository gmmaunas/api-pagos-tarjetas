package com.dbd.service.pagos_tarjetas.service;

import com.dbd.service.pagos_tarjetas.model.Pago;
import java.time.LocalDate;
import java.util.List;

/**
 * Interfaz del servicio para la gestión de Pagos
 */
public interface IPagoService {

    /**
     * Genera el total de pago de un mes dado, informando los items correspondientes
     * @param mes Mes del pago
     * @param anio Año del pago
     * @param primerVencimiento Fecha del primer vencimiento
     * @param segundoVencimiento Fecha del segundo vencimiento
     * @param recargoPrimero Recargo del primer vencimiento
     * @param recargoSegundo Recargo del segundo vencimiento
     * @return Pago generado
     */
    Pago generarPagoMensual(String mes, String anio, LocalDate primerVencimiento,
                            LocalDate segundoVencimiento, Double recargoPrimero,
                            Double recargoSegundo);

    /**
     * Edita las fechas de vencimiento de un pago con cierto código
     * @param codigo Código del pago
     * @param nuevoPrimerVencimiento Nueva fecha del primer vencimiento
     * @param nuevoSegundoVencimiento Nueva fecha del segundo vencimiento
     * @return Pago actualizado
     */
    Pago editarFechasVencimiento(String codigo, LocalDate nuevoPrimerVencimiento,
                                 LocalDate nuevoSegundoVencimiento);

    /**
     * Obtiene pago por código con todos sus items
     * @param codigo Código del pago
     * @return Pago con items
     */
    Pago obtenerPagoPorCodigoConItems(String codigo);

    /**
     * Obtiene un pago por su ID
     * @param id ID del pago
     * @return Pago encontrado
     */
    Pago obtenerPagoPorId(String id);

    /**
     * Obtiene un pago por su código
     * @param codigo Código del pago
     * @return Pago encontrado
     */
    Pago obtenerPagoPorCodigo(String codigo);

    /**
     * Obtiene todos los pagos
     * @return Lista de pagos
     */
    List<Pago> obtenerTodosLosPagos();

    /**
     * Obtiene los pagos de un año específico
     * @param anio Año de los pagos
     * @return Lista de pagos del año
     */
    List<Pago> obtenerPagosPorAnio(String anio);

    /**
     * Elimina un pago por su ID
     * @param id ID del pago a eliminar
     */
    void eliminarPago(String id);
}
