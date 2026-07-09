package com.dbd.service.pagos_tarjetas.service;

import com.dbd.service.pagos_tarjetas.model.Banco;
import com.dbd.service.pagos_tarjetas.model.TitularTarjeta;
import java.util.List;
import java.util.Map;

/**
 * Interfaz del servicio para la gestión de Bancos
 */
public interface IBancoService {

    /**
     * Crea un nuevo banco
     * @param banco Banco a crear
     * @return Banco creado
     */
    Banco crearBanco(Banco banco);

    /**
     * Obtiene un banco por su ID
     * @param id ID del banco
     * @return Banco encontrado
     */
    Banco obtenerBancoPorId(String id);

    /**
     * Obtiene un banco por su CUIT
     * @param cuit CUIT del banco
     * @return Banco encontrado
     */
    Banco obtenerBancoPorCuit(String cuit);

    /**
     * Obtiene todos los bancos
     * @return Lista de bancos
     */
    List<Banco> obtenerTodosLosBancos();

    /**
     * Actualiza un banco existente
     * @param id ID del banco a actualizar
     * @param bancoActualizado Datos actualizados del banco
     * @return Banco actualizado
     */
    Banco actualizarBanco(String id, Banco bancoActualizado);

    /**
     * Elimina un banco por su ID
     * @param id ID del banco a eliminar
     */
    void eliminarBanco(String id);

    /**
     * Obtiene el banco con mayor cantidad de compras realizadas con sus tarjetas
     * @return Banco con más compras
     */
    Banco obtenerBancoConMasCompras();

    /**
     * Obtiene el número de clientes de cada banco
     * @return Mapa con nombre del banco y cantidad de clientes
     */
    Map<String, Long> obtenerNumeroClientesPorBanco();

    /**
     * Obtiene los titulares de tarjeta que pertenecen a un banco dado
     * @param bancoId ID del banco
     * @return Lista de titulares del banco
     */
    List<TitularTarjeta> obtenerTitularesPorBanco(String bancoId);
}
