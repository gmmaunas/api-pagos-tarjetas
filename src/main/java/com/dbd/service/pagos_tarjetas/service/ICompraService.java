package com.dbd.service.pagos_tarjetas.service;

import com.dbd.service.pagos_tarjetas.model.Compra;
import com.dbd.service.pagos_tarjetas.model.CompraCuotas;
import com.dbd.service.pagos_tarjetas.model.CompraPagoUnico;
import com.dbd.service.pagos_tarjetas.model.Promocion;
import java.util.List;

/**
 * Interfaz del servicio para la gestión de Compras
 */
public interface ICompraService {

    /**
     * Crea una compra en un solo pago
     * @param compra Compra a crear
     * @return Compra creada
     */
    CompraPagoUnico crearCompraPagoUnico(CompraPagoUnico compra);

    /**
     * Crea una compra en cuotas
     * @param compra Compra a crear
     * @return Compra creada
     */
    CompraCuotas crearCompraCuotas(CompraCuotas compra);

    /**
     * Obtiene información de una compra con sus detalles completos
     * @param compraId ID de la compra
     * @return Compra con detalles
     */
    Compra obtenerCompraConDetalles(String compraId);

    /**
     * Obtiene una compra por su ID
     * @param id ID de la compra
     * @return Compra encontrada
     */
    Compra obtenerCompraPorId(String id);

    /**
     * Obtiene todas las compras
     * @return Lista de compras
     */
    List<Compra> obtenerTodasLasCompras();

    /**
     * Obtiene las compras de una tarjeta específica
     * @param tarjetaId ID de la tarjeta
     * @return Lista de compras de la tarjeta
     */
    List<Compra> obtenerComprasPorTarjeta(String tarjetaId);

    /**
     * Obtiene el nombre del local con mayor cantidad de compras
     * @return Nombre del local
     */
    String obtenerLocalConMasCompras();

    /**
     * Obtiene las promociones aplicadas a una compra específica
     * @param compraId ID de la compra
     * @return Lista de promociones aplicadas
     */
    List<Promocion> obtenerPromocionesPorCompra(String compraId);

    /**
     * Elimina una compra por su ID
     * @param id ID de la compra a eliminar
     */
    void eliminarCompra(String id);
}
