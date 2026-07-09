package com.dbd.service.pagos_tarjetas.rest.controller;

import com.dbd.service.pagos_tarjetas.mapper.BancoMapper;
import com.dbd.service.pagos_tarjetas.mapper.TitularTarjetaMapper;
import com.dbd.service.pagos_tarjetas.model.Banco;
import com.dbd.service.pagos_tarjetas.rest.request.BancoRequest;
import com.dbd.service.pagos_tarjetas.rest.response.BancoResponse;
import com.dbd.service.pagos_tarjetas.rest.response.TitularTarjetaResponse;
import com.dbd.service.pagos_tarjetas.service.IBancoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bancos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BancoController {

    private final IBancoService bancoService;

    @PostMapping
    public ResponseEntity<BancoResponse> crearBanco(@Valid @RequestBody BancoRequest request) {
        Banco banco = BancoMapper.toEntity(request);
        Banco nuevoBanco = bancoService.crearBanco(banco);
        return new ResponseEntity<>(BancoMapper.toResponse(nuevoBanco), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BancoResponse> obtenerBancoPorId(@PathVariable String id) {
        Banco banco = bancoService.obtenerBancoPorId(id);
        return ResponseEntity.ok(BancoMapper.toResponse(banco));
    }

    @GetMapping("/cuit/{cuit}")
    public ResponseEntity<BancoResponse> obtenerBancoPorCuit(@PathVariable String cuit) {
        Banco banco = bancoService.obtenerBancoPorCuit(cuit);
        return ResponseEntity.ok(BancoMapper.toResponse(banco));
    }

    @GetMapping
    public ResponseEntity<List<BancoResponse>> obtenerTodosLosBancos() {
        List<Banco> bancos = bancoService.obtenerTodosLosBancos();
        List<BancoResponse> response = bancos.stream()
                .map(BancoMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BancoResponse> actualizarBanco(
            @PathVariable String id,
            @Valid @RequestBody BancoRequest request) {
        Banco banco = bancoService.obtenerBancoPorId(id);
        BancoMapper.updateEntityFromRequest(banco, request);
        Banco bancoActualizado = bancoService.actualizarBanco(id, banco);
        return ResponseEntity.ok(BancoMapper.toResponse(bancoActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarBanco(@PathVariable String id) {
        bancoService.eliminarBanco(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mas-compras")
    public ResponseEntity<BancoResponse> obtenerBancoConMasCompras() {
        Banco banco = bancoService.obtenerBancoConMasCompras();
        return ResponseEntity.ok(BancoMapper.toResponse(banco));
    }

    @GetMapping("/clientes-por-banco")
    public ResponseEntity<Map<String, Long>> obtenerNumeroClientesPorBanco() {
        Map<String, Long> clientesPorBanco = bancoService.obtenerNumeroClientesPorBanco();
        return ResponseEntity.ok(clientesPorBanco);
    }

    @GetMapping("/{id}/titulares")
    public ResponseEntity<List<TitularTarjetaResponse>> obtenerTitularesPorBanco(@PathVariable String id) {
        List<TitularTarjetaResponse> response = bancoService.obtenerTitularesPorBanco(id).stream()
                .map(TitularTarjetaMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}
