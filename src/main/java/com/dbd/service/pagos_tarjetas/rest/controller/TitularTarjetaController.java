package com.dbd.service.pagos_tarjetas.rest.controller;

import com.dbd.service.pagos_tarjetas.mapper.TitularTarjetaMapper;
import com.dbd.service.pagos_tarjetas.model.Banco;
import com.dbd.service.pagos_tarjetas.model.TitularTarjeta;
import com.dbd.service.pagos_tarjetas.rest.request.TitularTarjetaRequest;
import com.dbd.service.pagos_tarjetas.rest.response.TitularTarjetaResponse;
import com.dbd.service.pagos_tarjetas.service.IBancoService;
import com.dbd.service.pagos_tarjetas.service.ITitularTarjetaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/titulares")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TitularTarjetaController {

    private final ITitularTarjetaService titularTarjetaService;
    private final IBancoService bancoService;

    @PostMapping
    public ResponseEntity<TitularTarjetaResponse> crearTitular(@Valid @RequestBody TitularTarjetaRequest request) {
        Banco banco = bancoService.obtenerBancoPorId(request.bancoId());
        TitularTarjeta titular = TitularTarjetaMapper.toEntity(request, banco);
        TitularTarjeta nuevoTitular = titularTarjetaService.crearTitular(titular);
        return new ResponseEntity<>(TitularTarjetaMapper.toResponse(nuevoTitular), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TitularTarjetaResponse> obtenerTitularPorId(@PathVariable Long id) {
        TitularTarjeta titular = titularTarjetaService.obtenerTitularPorId(id);
        return ResponseEntity.ok(TitularTarjetaMapper.toResponse(titular));
    }

    @GetMapping("/cuit/{cuit}")
    public ResponseEntity<TitularTarjetaResponse> obtenerTitularPorCuit(@PathVariable String cuit) {
        TitularTarjeta titular = titularTarjetaService.obtenerTitularPorCuit(cuit);
        return ResponseEntity.ok(TitularTarjetaMapper.toResponse(titular));
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<TitularTarjetaResponse> obtenerTitularPorDni(@PathVariable String dni) {
        TitularTarjeta titular = titularTarjetaService.obtenerTitularPorDni(dni);
        return ResponseEntity.ok(TitularTarjetaMapper.toResponse(titular));
    }

    @GetMapping
    public ResponseEntity<List<TitularTarjetaResponse>> obtenerTodosLosTitulares() {
        List<TitularTarjeta> titulares = titularTarjetaService.obtenerTodosLosTitulares();
        List<TitularTarjetaResponse> response = titulares.stream()
                .map(TitularTarjetaMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/banco/{bancoId}")
    public ResponseEntity<List<TitularTarjetaResponse>> obtenerTitularesPorBanco(@PathVariable Long bancoId) {
        List<TitularTarjeta> titulares = titularTarjetaService.obtenerTitularesPorBanco(bancoId);
        List<TitularTarjetaResponse> response = titulares.stream()
                .map(TitularTarjetaMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TitularTarjetaResponse> actualizarTitular(
            @PathVariable Long id,
            @Valid @RequestBody TitularTarjetaRequest request) {
        TitularTarjeta titular = titularTarjetaService.obtenerTitularPorId(id);
        Banco banco = bancoService.obtenerBancoPorId(request.bancoId());
        TitularTarjetaMapper.updateEntityFromRequest(titular, request, banco);
        TitularTarjeta titularActualizado = titularTarjetaService.actualizarTitular(id, titular);
        return ResponseEntity.ok(TitularTarjetaMapper.toResponse(titularActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTitular(@PathVariable Long id) {
        titularTarjetaService.eliminarTitular(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/top-compradores")
    public ResponseEntity<Map<String, Double>> obtenerTopCompradoresConMayorMonto(
            @RequestParam(defaultValue = "10") int limite) {
        Map<String, Double> topCompradores = titularTarjetaService.obtenerTopNTitularesConMayorMontoCompras(limite);
        return ResponseEntity.ok(topCompradores);
    }
}
