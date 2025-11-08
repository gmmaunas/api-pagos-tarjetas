package com.dbd.service.pagos_tarjetas.rest.controller;

import com.dbd.service.pagos_tarjetas.mapper.PagoMapper;
import com.dbd.service.pagos_tarjetas.model.Pago;
import com.dbd.service.pagos_tarjetas.rest.request.EditarFechasVencimientoRequest;
import com.dbd.service.pagos_tarjetas.rest.request.PagoRequest;
import com.dbd.service.pagos_tarjetas.rest.response.PagoResponse;
import com.dbd.service.pagos_tarjetas.service.IPagoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pagos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PagoController {

    private final IPagoService pagoService;

    @PostMapping("/generar")
    public ResponseEntity<PagoResponse> generarPagoMensual(@Valid @RequestBody PagoRequest request) {
        Pago pago = pagoService.generarPagoMensual(
                request.mes(), request.anio(), request.primerVencimiento(),
                request.segundoVencimiento(), request.recargoPrimerVencimiento(),
                request.recargoSegundoVencimiento());
        return new ResponseEntity<>(PagoMapper.toResponse(pago), HttpStatus.CREATED);
    }

    @PutMapping("/codigo/{codigo}/fechas-vencimiento")
    public ResponseEntity<PagoResponse> editarFechasVencimiento(
            @PathVariable String codigo,
            @Valid @RequestBody EditarFechasVencimientoRequest request) {
        Pago pago = pagoService.editarFechasVencimiento(codigo,
                request.primerVencimiento(), request.segundoVencimiento());
        return ResponseEntity.ok(PagoMapper.toResponse(pago));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoResponse> obtenerPagoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(PagoMapper.toResponse(pagoService.obtenerPagoPorId(id)));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<PagoResponse> obtenerPagoPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(PagoMapper.toResponse(pagoService.obtenerPagoPorCodigo(codigo)));
    }

    @GetMapping("/codigo/{codigo}/items")
    public ResponseEntity<PagoResponse> obtenerPagoPorCodigoConItems(@PathVariable String codigo) {
        return ResponseEntity.ok(PagoMapper.toResponse(pagoService.obtenerPagoPorCodigoConItems(codigo)));
    }

    @GetMapping
    public ResponseEntity<List<PagoResponse>> obtenerTodosLosPagos() {
        List<PagoResponse> response = pagoService.obtenerTodosLosPagos().stream()
                .map(PagoMapper::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/anio/{anio}")
    public ResponseEntity<List<PagoResponse>> obtenerPagosPorAnio(@PathVariable String anio) {
        List<PagoResponse> response = pagoService.obtenerPagosPorAnio(anio).stream()
                .map(PagoMapper::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPago(@PathVariable Long id) {
        pagoService.eliminarPago(id);
        return ResponseEntity.noContent().build();
    }
}
