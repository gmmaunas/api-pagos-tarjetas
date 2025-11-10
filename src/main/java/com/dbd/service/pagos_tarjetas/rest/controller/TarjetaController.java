package com.dbd.service.pagos_tarjetas.rest.controller;

import com.dbd.service.pagos_tarjetas.mapper.TarjetaMapper;
import com.dbd.service.pagos_tarjetas.model.Banco;
import com.dbd.service.pagos_tarjetas.model.Tarjeta;
import com.dbd.service.pagos_tarjetas.model.TitularTarjeta;
import com.dbd.service.pagos_tarjetas.rest.request.TarjetaRequest;
import com.dbd.service.pagos_tarjetas.rest.response.TarjetaResponse;
import com.dbd.service.pagos_tarjetas.service.IBancoService;
import com.dbd.service.pagos_tarjetas.service.ITarjetaService;
import com.dbd.service.pagos_tarjetas.service.ITitularTarjetaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tarjetas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TarjetaController {

    private final ITarjetaService tarjetaService;
    private final ITitularTarjetaService titularTarjetaService;
    private final IBancoService bancoService;

    @PostMapping
    public ResponseEntity<TarjetaResponse> crearTarjeta(@Valid @RequestBody TarjetaRequest request) {
        TitularTarjeta titular = titularTarjetaService.obtenerTitularPorId(request.titularId());
        Banco banco = bancoService.obtenerBancoPorId(request.bancoId());
        Tarjeta tarjeta = TarjetaMapper.toEntity(request, titular, banco);
        Tarjeta nuevaTarjeta = tarjetaService.crearTarjeta(tarjeta);
        return new ResponseEntity<>(TarjetaMapper.toResponse(nuevaTarjeta), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TarjetaResponse> obtenerTarjetaPorId(@PathVariable String id) {
        Tarjeta tarjeta = tarjetaService.obtenerTarjetaPorId(id);
        return ResponseEntity.ok(TarjetaMapper.toResponse(tarjeta));
    }

    @GetMapping("/numero/{numero}")
    public ResponseEntity<TarjetaResponse> obtenerTarjetaPorNumero(@PathVariable String numero) {
        Tarjeta tarjeta = tarjetaService.obtenerTarjetaPorNumero(numero);
        return ResponseEntity.ok(TarjetaMapper.toResponse(tarjeta));
    }

    @GetMapping
    public ResponseEntity<List<TarjetaResponse>> obtenerTodasLasTarjetas() {
        List<Tarjeta> tarjetas = tarjetaService.obtenerTodasLasTarjetas();
        List<TarjetaResponse> response = tarjetas.stream()
                .map(TarjetaMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/titular/{titularId}")
    public ResponseEntity<List<TarjetaResponse>> obtenerTarjetasPorTitular(@PathVariable String titularId) {
        List<Tarjeta> tarjetas = tarjetaService.obtenerTarjetasPorTitular(titularId);
        List<TarjetaResponse> response = tarjetas.stream()
                .map(TarjetaMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/banco/{bancoId}")
    public ResponseEntity<List<TarjetaResponse>> obtenerTarjetasPorBanco(@PathVariable String bancoId) {
        List<Tarjeta> tarjetas = tarjetaService.obtenerTarjetasPorBanco(bancoId);
        List<TarjetaResponse> response = tarjetas.stream()
                .map(TarjetaMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarjetaResponse> actualizarTarjeta(
            @PathVariable String id,
            @Valid @RequestBody TarjetaRequest request) {
        Tarjeta tarjeta = tarjetaService.obtenerTarjetaPorId(id);
        TitularTarjeta titular = titularTarjetaService.obtenerTitularPorId(request.titularId());
        Banco banco = bancoService.obtenerBancoPorId(request.bancoId());
        TarjetaMapper.updateEntityFromRequest(tarjeta, request, titular, banco);
        Tarjeta tarjetaActualizada = tarjetaService.actualizarTarjeta(id, tarjeta);
        return ResponseEntity.ok(TarjetaMapper.toResponse(tarjetaActualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTarjeta(@PathVariable String id) {
        tarjetaService.eliminarTarjeta(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/emitidas")
    public ResponseEntity<List<TarjetaResponse>> obtenerTarjetasEmitidas(
            @RequestParam(defaultValue = "5") int anios) {
        List<Tarjeta> tarjetas = tarjetaService.obtenerTarjetasEmitidasHaceMasDeNAnios(anios);
        List<TarjetaResponse> response = tarjetas.stream()
                .map(TarjetaMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}
