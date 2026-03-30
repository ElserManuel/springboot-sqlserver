package ap2.ElserManuelLazaroRosas.service;

import ap2.ElserManuelLazaroRosas.model.Pedido;
import ap2.ElserManuelLazaroRosas.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository repository;

    public List<Pedido> listar() {
        return repository.findAll();
    }

    public Optional<Pedido> buscarPorId(int id) {
        return repository.findById(id);
    }

    public Pedido guardar(Pedido p) {
        if (p.getFechaPedido() == null)
            p.setFechaPedido(LocalDateTime.now());
        return repository.save(p);
    }

    public Pedido actualizar(int id, Pedido p) {
        return repository.findById(id).map(existing -> {
            existing.setEstado(p.getEstado());
            existing.setMetodoPago(p.getMetodoPago());
            existing.setObservacion(p.getObservacion());
            existing.setSubtotal(p.getSubtotal());
            existing.setDescuento(p.getDescuento());
            existing.setIgv(p.getIgv());
            existing.setTotal(p.getTotal());
            return repository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Pedido no encontrado: " + id));
    }

    public void eliminar(int id) {
        repository.deleteById(id);
    }

    public List<Pedido> listarPorCliente(int idCliente) {
        return repository.findByCliente_IdClienteOrderByFechaPedidoDesc(idCliente);
    }

    public List<Pedido> listarPorEstado(String estado) {
        return repository.findByEstado(estado);
    }
}