package com.dbd.service.pagos_tarjetas.rest.controller;

import com.dbd.service.pagos_tarjetas.mapper.CompraMapper;
import com.dbd.service.pagos_tarjetas.model.*;
import com.dbd.service.pagos_tarjetas.rest.request.CompraCuotasRequest;
import com.dbd.service.pagos_tarjetas.rest.request.CompraPagoUnicoRequest;
import com.dbd.service.pagos_tarjetas.mapper.PromocionMapper;
import com.dbd.service.pagos_tarjetas.rest.response.CompraResponse;
import com.dbd.service.pagos_tarjetas.rest.response.CompraCuotasResponse;
import com.dbd.service.pagos_tarjetas.rest.response.CompraPagoUnicoResponse;
import com.dbd.service.pagos_tarjetas.rest.response.PromocionResponse;
import com.dbd.service.pagos_tarjetas.service.ICompraService;
import com.dbd.service.pagos_tarjetas.service.ITarjetaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/compras")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CompraController {

    private final ICompraService compraService;
    private final ITarjetaService tarjetaService;

    @PostMapping("/pago-unico")
    public ResponseEntity<CompraPagoUnicoResponse> crearCompraPagoUnico(
            @Valid @RequestBody CompraPagoUnicoRequest request) {
        Tarjeta tarjeta = tarjetaService.obtenerTarjetaPorId(request.tarjetaId());
        CompraPagoUnico compra = CompraMapper.toEntity(request, tarjeta, null);
        CompraPagoUnico nuevaCompra = compraService.crearCompraPagoUnico(compra);
        return new ResponseEntity<>(CompraMapper.toPagoUnicoResponse(nuevaCompra), HttpStatus.CREATED);
    }

    @PostMapping("/cuotas")
    public ResponseEntity<CompraCuotasResponse> crearCompraCuotas(
            @Valid @RequestBody CompraCuotasRequest request) {
        Tarjeta tarjeta = tarjetaService.obtenerTarjetaPorId(request.tarjetaId());
        CompraCuotas compra = CompraMapper.toEntity(request, tarjeta, null);
        CompraCuotas nuevaCompra = compraService.crearCompraCuotas(compra);
        return new ResponseEntity<>(CompraMapper.toCuotasResponse(nuevaCompra), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompraResponse> obtenerCompraPorId(@PathVariable String id) {
        Compra compra = compraService.obtenerCompraPorId(id);
        return ResponseEntity.ok(CompraMapper.toResponse(compra));
    }

    @GetMapping("/{id}/detalles")
    public ResponseEntity<?> obtenerCompraConDetalles(@PathVariable String id) {
        Compra compra = compraService.obtenerCompraConDetalles(id);

        return ResponseEntity.ok(switch (compra) {
            case CompraPagoUnico cpu -> CompraMapper.toPagoUnicoResponse(cpu);
            case CompraCuotas cc -> CompraMapper.toCuotasResponse(cc);
            default -> CompraMapper.toResponse(compra);
        });
    }

    @GetMapping
    public ResponseEntity<List<CompraResponse>> obtenerTodasLasCompras() {
        List<Compra> compras = compraService.obtenerTodasLasCompras();
        List<CompraResponse> response = compras.stream()
                .map(CompraMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/tarjeta/{tarjetaId}")
    public ResponseEntity<List<CompraResponse>> obtenerComprasPorTarjeta(@PathVariable String tarjetaId) {
        List<Compra> compras = compraService.obtenerComprasPorTarjeta(tarjetaId);
        List<CompraResponse> response = compras.stream()
                .map(CompraMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/local-mas-compras")
    public ResponseEntity<String> obtenerLocalConMasCompras() {
        String local = compraService.obtenerLocalConMasCompras();
        return ResponseEntity.ok(local);
    }

    @GetMapping("/{id}/promociones")
    public ResponseEntity<List<PromocionResponse>> obtenerPromocionesPorCompra(@PathVariable String id) {
        List<Promocion> promociones = compraService.obtenerPromocionesPorCompra(id);
        List<PromocionResponse> response = promociones.stream()
                .map(PromocionMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCompra(@PathVariable String id) {
        compraService.eliminarCompra(id);
        return ResponseEntity.noContent().build();
    }
}
