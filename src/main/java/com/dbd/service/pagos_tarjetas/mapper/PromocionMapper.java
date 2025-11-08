package com.dbd.service.pagos_tarjetas.mapper;

import com.dbd.service.pagos_tarjetas.model.Banco;
import com.dbd.service.pagos_tarjetas.model.Descuento;
import com.dbd.service.pagos_tarjetas.model.Financiacion;
import com.dbd.service.pagos_tarjetas.model.Promocion;
import com.dbd.service.pagos_tarjetas.rest.request.DescuentoRequest;
import com.dbd.service.pagos_tarjetas.rest.request.FinanciacionRequest;
import com.dbd.service.pagos_tarjetas.rest.response.DescuentoResponse;
import com.dbd.service.pagos_tarjetas.rest.response.FinanciacionResponse;
import com.dbd.service.pagos_tarjetas.rest.response.PromocionResponse;

public class PromocionMapper {

    private PromocionMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    // Descuento
    public static Descuento toEntity(DescuentoRequest request, Banco banco) {
        if (request == null) {
            return null;
        }

        return Descuento.builder()
                .codigo(request.codigo())
                .tituloPromocion(request.tituloPromocion())
                .nombreTienda(request.nombreTienda())
                .cuitTienda(request.cuitTienda())
                .fechaInicioValidez(request.fechaInicioValidez())
                .fechaFinValidez(request.fechaFinValidez())
                .comentarios(request.comentarios())
                .banco(banco)
                .porcentajeDescuento(request.porcentajeDescuento())
                .tope(request.tope())
                .soloContado(request.soloContado() != null ? request.soloContado() : false)
                .build();
    }

    public static DescuentoResponse toDescuentoResponse(Descuento descuento) {
        if (descuento == null) {
            return null;
        }

        return new DescuentoResponse(
                descuento.getId(),
                descuento.getCodigo(),
                descuento.getTituloPromocion(),
                descuento.getNombreTienda(),
                descuento.getCuitTienda(),
                descuento.getFechaInicioValidez(),
                descuento.getFechaFinValidez(),
                descuento.getComentarios(),
                descuento.getBanco() != null ? descuento.getBanco().getId() : null,
                descuento.getBanco() != null ? descuento.getBanco().getNombre() : null,
                descuento.getPorcentajeDescuento(),
                descuento.getTope(),
                descuento.getSoloContado()
        );
    }

    // Financiación
    public static Financiacion toEntity(FinanciacionRequest request, Banco banco) {
        if (request == null) {
            return null;
        }

        return Financiacion.builder()
                .codigo(request.codigo())
                .tituloPromocion(request.tituloPromocion())
                .nombreTienda(request.nombreTienda())
                .cuitTienda(request.cuitTienda())
                .fechaInicioValidez(request.fechaInicioValidez())
                .fechaFinValidez(request.fechaFinValidez())
                .comentarios(request.comentarios())
                .banco(banco)
                .numeroCuotas(request.numeroCuotas())
                .interes(request.interes())
                .build();
    }

    public static FinanciacionResponse toFinanciacionResponse(Financiacion financiacion) {
        if (financiacion == null) {
            return null;
        }

        return new FinanciacionResponse(
                financiacion.getId(),
                financiacion.getCodigo(),
                financiacion.getTituloPromocion(),
                financiacion.getNombreTienda(),
                financiacion.getCuitTienda(),
                financiacion.getFechaInicioValidez(),
                financiacion.getFechaFinValidez(),
                financiacion.getComentarios(),
                financiacion.getBanco() != null ? financiacion.getBanco().getId() : null,
                financiacion.getBanco() != null ? financiacion.getBanco().getNombre() : null,
                financiacion.getNumeroCuotas(),
                financiacion.getInteres()
        );
    }

    // Promoción genérica
    public static PromocionResponse toResponse(Promocion promocion) {
        if (promocion == null) {
            return null;
        }

        return new PromocionResponse(
                promocion.getId(),
                promocion.getTipo(),
                promocion.getCodigo(),
                promocion.getTituloPromocion(),
                promocion.getNombreTienda(),
                promocion.getCuitTienda(),
                promocion.getFechaInicioValidez(),
                promocion.getFechaFinValidez(),
                promocion.getComentarios(),
                promocion.getBanco() != null ? promocion.getBanco().getId() : null,
                promocion.getBanco() != null ? promocion.getBanco().getNombre() : null
        );
    }

    public static void updateDescuentoFromRequest(Descuento descuento, DescuentoRequest request, Banco banco) {
        if (descuento == null || request == null) {
            return;
        }

        descuento.setCodigo(request.codigo());
        descuento.setTituloPromocion(request.tituloPromocion());
        descuento.setNombreTienda(request.nombreTienda());
        descuento.setCuitTienda(request.cuitTienda());
        descuento.setFechaInicioValidez(request.fechaInicioValidez());
        descuento.setFechaFinValidez(request.fechaFinValidez());
        descuento.setComentarios(request.comentarios());
        descuento.setPorcentajeDescuento(request.porcentajeDescuento());
        descuento.setTope(request.tope());
        descuento.setSoloContado(request.soloContado() != null ? request.soloContado() : false);
        if (banco != null) {
            descuento.setBanco(banco);
        }
    }

    public static void updateFinanciacionFromRequest(Financiacion financiacion, FinanciacionRequest request, Banco banco) {
        if (financiacion == null || request == null) {
            return;
        }

        financiacion.setCodigo(request.codigo());
        financiacion.setTituloPromocion(request.tituloPromocion());
        financiacion.setNombreTienda(request.nombreTienda());
        financiacion.setCuitTienda(request.cuitTienda());
        financiacion.setFechaInicioValidez(request.fechaInicioValidez());
        financiacion.setFechaFinValidez(request.fechaFinValidez());
        financiacion.setComentarios(request.comentarios());
        financiacion.setNumeroCuotas(request.numeroCuotas());
        financiacion.setInteres(request.interes());
        if (banco != null) {
            financiacion.setBanco(banco);
        }
    }
}
