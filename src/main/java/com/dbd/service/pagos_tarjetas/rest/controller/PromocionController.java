package com.dbd.service.pagos_tarjetas.rest.controller;

import com.dbd.service.pagos_tarjetas.mapper.PromocionMapper;
import com.dbd.service.pagos_tarjetas.model.Banco;
import com.dbd.service.pagos_tarjetas.model.Descuento;
import com.dbd.service.pagos_tarjetas.model.Financiacion;
import com.dbd.service.pagos_tarjetas.model.Promocion;
import com.dbd.service.pagos_tarjetas.rest.request.DescuentoRequest;
import com.dbd.service.pagos_tarjetas.rest.request.FinanciacionRequest;
import com.dbd.service.pagos_tarjetas.rest.request.PromocionUpdateRequest;
import com.dbd.service.pagos_tarjetas.rest.response.DescuentoResponse;
import com.dbd.service.pagos_tarjetas.rest.response.FinanciacionResponse;
import com.dbd.service.pagos_tarjetas.rest.response.PromocionResponse;
import com.dbd.service.pagos_tarjetas.service.IBancoService;
import com.dbd.service.pagos_tarjetas.service.IPromocionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/promociones")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PromocionController {

    private final IPromocionService promocionService;
    private final IBancoService bancoService;

    @PostMapping("/descuento")
    public ResponseEntity<DescuentoResponse> agregarDescuento(@Valid @RequestBody DescuentoRequest request) {
        Banco banco = bancoService.obtenerBancoPorId(request.bancoId());
        Descuento descuento = PromocionMapper.toEntity(request, banco);
        Descuento nuevoDescuento = promocionService.agregarDescuento(descuento);
        return new ResponseEntity<>(PromocionMapper.toDescuentoResponse(nuevoDescuento), HttpStatus.CREATED);
    }

    @PostMapping("/financiacion")
    public ResponseEntity<FinanciacionResponse> agregarFinanciacion(@Valid @RequestBody FinanciacionRequest request) {
        Banco banco = bancoService.obtenerBancoPorId(request.bancoId());
        Financiacion financiacion = PromocionMapper.toEntity(request, banco);
        Financiacion nuevaFinanciacion = promocionService.agregarFinanciacion(financiacion);
        return new ResponseEntity<>(PromocionMapper.toFinanciacionResponse(nuevaFinanciacion), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPromocionPorId(@PathVariable String id) {
        Promocion promocion = promocionService.obtenerPromocionPorId(id);

        return ResponseEntity.ok(switch (promocion) {
            case Descuento d -> PromocionMapper.toDescuentoResponse(d);
            case Financiacion f -> PromocionMapper.toFinanciacionResponse(f);
            default -> PromocionMapper.toResponse(promocion);
        });
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<?> obtenerPromocionPorCodigo(@PathVariable String codigo) {
        Promocion promocion = promocionService.obtenerPromocionPorCodigo(codigo);

        return ResponseEntity.ok(switch (promocion) {
            case Descuento d -> PromocionMapper.toDescuentoResponse(d);
            case Financiacion f -> PromocionMapper.toFinanciacionResponse(f);
            default -> PromocionMapper.toResponse(promocion);
        });
    }

    @GetMapping
    public ResponseEntity<List<PromocionResponse>> obtenerTodasLasPromociones() {
        List<Promocion> promociones = promocionService.obtenerTodasLasPromociones();
        List<PromocionResponse> response = promociones.stream()
                .map(PromocionMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/banco/{bancoId}")
    public ResponseEntity<List<PromocionResponse>> obtenerPromocionesPorBanco(@PathVariable String bancoId) {
        List<Promocion> promociones = promocionService.obtenerPromocionesPorBanco(bancoId);
        List<PromocionResponse> response = promociones.stream()
                .map(PromocionMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/local/{cuitTienda}")
    public ResponseEntity<List<PromocionResponse>> obtenerPromocionesDisponiblesPorLocalYFechas(
            @PathVariable String cuitTienda,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<Promocion> promociones = promocionService.obtenerPromocionesDisponiblesPorLocalYFechas(
                cuitTienda, fechaInicio, fechaFin);
        List<PromocionResponse> response = promociones.stream()
                .map(PromocionMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarPromocion(
            @PathVariable String id,
            @Valid @RequestBody PromocionUpdateRequest request) {
        Promocion actual = promocionService.obtenerPromocionPorId(id);
        actual.setTituloPromocion(request.tituloPromocion());
        actual.setFechaInicioValidez(request.fechaInicioValidez());
        actual.setFechaFinValidez(request.fechaFinValidez());
        actual.setComentarios(request.comentarios());
        Promocion actualizada = promocionService.actualizarPromocion(id, actual);
        return ResponseEntity.ok(switch (actualizada) {
            case Descuento d -> PromocionMapper.toDescuentoResponse(d);
            case Financiacion f -> PromocionMapper.toFinanciacionResponse(f);
            default -> PromocionMapper.toResponse(actualizada);
        });
    }

    @DeleteMapping("/codigo/{codigo}")
    public ResponseEntity<Void> eliminarPromocionPorCodigo(@PathVariable String codigo) {
        promocionService.eliminarPromocionPorCodigo(codigo);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPromocion(@PathVariable String id) {
        promocionService.eliminarPromocion(id);
        return ResponseEntity.noContent().build();
    }
}
