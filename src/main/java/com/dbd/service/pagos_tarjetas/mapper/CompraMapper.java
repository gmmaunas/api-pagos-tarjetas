package com.dbd.service.pagos_tarjetas.mapper;

import com.dbd.service.pagos_tarjetas.model.*;
import com.dbd.service.pagos_tarjetas.rest.request.CompraCuotasRequest;
import com.dbd.service.pagos_tarjetas.rest.request.CompraPagoUnicoRequest;
import com.dbd.service.pagos_tarjetas.rest.response.*;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class CompraMapper {

    private CompraMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    // CompraPagoUnico
    public static CompraPagoUnico toEntity(CompraPagoUnicoRequest request, Tarjeta tarjeta) {
        if (request == null) {
            return null;
        }

        return CompraPagoUnico.builder()
                .comprobanteVoucher(request.comprobanteVoucher())
                .tienda(request.tienda())
                .cuitTienda(request.cuitTienda())
                .monto(request.monto())
                .fechaHora(request.fechaHora())
                .tarjeta(tarjeta)
                .descuentoTienda(request.descuentoTienda() != null ? request.descuentoTienda() : 0.0)
                .build();
    }

    public static CompraPagoUnicoResponse toPagoUnicoResponse(CompraPagoUnico compra) {
        if (compra == null) {
            return null;
        }

        return new CompraPagoUnicoResponse(
                compra.getId(),
                compra.getComprobanteVoucher(),
                compra.getTienda(),
                compra.getCuitTienda(),
                compra.getMonto(),
                compra.getMontoFinal(),
                compra.getFechaHora(),
                compra.getTarjeta() != null ? compra.getTarjeta().getId() : null,
                compra.getTarjeta() != null ? compra.getTarjeta().getNumero() : null,
                compra.getDescuentoTienda(),
                compra.getPago() != null ? compra.getPago().getId() : null,
                PromocionMapper.toResponse(compra.getPromocionAplicada())
        );
    }

    // CompraCuotas
    public static CompraCuotas toEntity(CompraCuotasRequest request, Tarjeta tarjeta) {
        if (request == null) {
            return null;
        }

        return CompraCuotas.builder()
                .comprobanteVoucher(request.comprobanteVoucher())
                .tienda(request.tienda())
                .cuitTienda(request.cuitTienda())
                .monto(request.monto())
                .fechaHora(request.fechaHora())
                .tarjeta(tarjeta)
                .interes(request.interes() != null ? request.interes() : 0.0)
                .numeroCuotas(request.numeroCuotas())
                .build();
    }

    public static CompraCuotasResponse toCuotasResponse(CompraCuotas compra) {
        if (compra == null) {
            return null;
        }

        return new CompraCuotasResponse(
                compra.getId(),
                compra.getComprobanteVoucher(),
                compra.getTienda(),
                compra.getCuitTienda(),
                compra.getMonto(),
                compra.getMontoFinal(),
                compra.getFechaHora(),
                compra.getTarjeta() != null ? compra.getTarjeta().getId() : null,
                compra.getTarjeta() != null ? compra.getTarjeta().getNumero() : null,
                compra.getInteres(),
                compra.getNumeroCuotas(),
                compra.getCuotas() != null
                        ? compra.getCuotas().stream()
                        .map(CompraMapper::toCuotaResponse)
                        .collect(Collectors.toList())
                        : new ArrayList<>(),
                PromocionMapper.toResponse(compra.getPromocionAplicada())
        );
    }

    // Compra genérica
    public static CompraResponse toResponse(Compra compra) {
        if (compra == null) {
            return null;
        }

        return new CompraResponse(
                compra.getId(),
                compra.getTipo(),
                compra.getComprobanteVoucher(),
                compra.getTienda(),
                compra.getCuitTienda(),
                compra.getMonto(),
                compra.getMontoFinal(),
                compra.getFechaHora(),
                compra.getTarjeta() != null ? compra.getTarjeta().getId() : null,
                compra.getTarjeta() != null ? compra.getTarjeta().getNumero() : null,
                PromocionMapper.toResponse(compra.getPromocionAplicada())
        );
    }

    // Cuota
    public static CuotaResponse toCuotaResponse(Cuota cuota) {
        if (cuota == null) {
            return null;
        }

        return new CuotaResponse(
                cuota.getId(),
                cuota.getNumero(),
                cuota.getPrecio(),
                cuota.getMes(),
                cuota.getAnio(),
                cuota.getCompraId(),
                cuota.getPago() != null ? cuota.getPago().getId() : null
        );
    }
}
