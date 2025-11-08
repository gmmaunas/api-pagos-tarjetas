package com.dbd.service.pagos_tarjetas.service.impl;

import com.dbd.service.pagos_tarjetas.model.Banco;
import com.dbd.service.pagos_tarjetas.repository.BancoRepository;
import com.dbd.service.pagos_tarjetas.service.IBancoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Transactional
public class BancoServiceImpl implements IBancoService {
    
    private final BancoRepository bancoRepository;
    
    public Banco crearBanco(Banco banco) {
        return bancoRepository.save(banco);
    }
    
    @Override
    public Banco obtenerBancoPorId(Long id) {
        return bancoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Banco no encontrado con id: " + id));
    }
    
    @Override
    public Banco obtenerBancoPorCuit(String cuit) {
        return bancoRepository.findByCuit(cuit)
            .orElseThrow(() -> new RuntimeException("Banco no encontrado con CUIT: " + cuit));
    }

    @Override
    public List<Banco> obtenerTodosLosBancos() {
        return bancoRepository.findAll();
    }

    @Override
    public Banco actualizarBanco(Long id, Banco bancoActualizado) {
        Banco banco = obtenerBancoPorId(id);
        banco.setNombre(bancoActualizado.getNombre());
        banco.setDireccion(bancoActualizado.getDireccion());
        banco.setTelefono(bancoActualizado.getTelefono());
        banco.setDireccionWeb(bancoActualizado.getDireccionWeb());
        return bancoRepository.save(banco);
    }

    @Override
    public void eliminarBanco(Long id) {
        bancoRepository.deleteById(id);
    }

    // Obtener el banco con mayor cantidad de compras realizadas con sus tarjetas
    @Override
    public Banco obtenerBancoConMasCompras() {
        List<Banco> bancos = bancoRepository.findBancoConMasCompras();
        if (bancos.isEmpty()) {
            throw new RuntimeException("No se encontraron bancos con compras");
        }
        return bancos.get(0);
    }
    
    // Obtener el número de clientes de cada banco
    @Override
    public Map<String, Long> obtenerNumeroClientesPorBanco() {
        List<Object[]> resultados = bancoRepository.contarClientesPorBanco();
        Map<String, Long> clientesPorBanco = new HashMap<>();
        
        for (Object[] resultado : resultados) {
            String nombreBanco = (String) resultado[0];
            Long cantidadClientes = (Long) resultado[1];
            clientesPorBanco.put(nombreBanco, cantidadClientes);
        }
        
        return clientesPorBanco;
    }
}
