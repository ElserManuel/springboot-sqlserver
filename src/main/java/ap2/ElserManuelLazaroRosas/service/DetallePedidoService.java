package ap2.ElserManuelLazaroRosas.service;

import ap2.ElserManuelLazaroRosas.model.DetallePedido;
import ap2.ElserManuelLazaroRosas.repository.DetallePedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DetallePedidoService {

    private final DetallePedidoRepository repository;

    public List<DetallePedido> listar() {
        return repository.findAll();
    }

    public Optional<DetallePedido> buscarPorId(int id) {
        return repository.findById(id);
    }

    public List<DetallePedido> listarPorPedido(int idPedido) {
        return repository.findByPedido_IdPedido(idPedido);
    }

    public DetallePedido guardar(DetallePedido d) {
        return repository.save(d);
    }

    public void eliminar(int id) {
        repository.deleteById(id);
    }

    public void eliminarPorPedido(int idPedido) {
        repository.deleteByPedido_IdPedido(idPedido);
    }
}