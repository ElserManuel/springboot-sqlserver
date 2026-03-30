package ap2.ElserManuelLazaroRosas.service.impl;

import ap2.ElserManuelLazaroRosas.model.DetallePedido;
import ap2.ElserManuelLazaroRosas.repository.DetallePedidoRepository;
import ap2.ElserManuelLazaroRosas.service.DetallePedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DetallePedidoServiceImpl implements DetallePedidoService {

    private final DetallePedidoRepository repository;

    @Override
    public List<DetallePedido> listar() {
        return repository.findAll();
    }

    @Override
    public Optional<DetallePedido> buscarPorId(int id) {
        return repository.findById(id);
    }

    @Override
    public List<DetallePedido> listarPorPedido(int idPedido) {
        return repository.findByPedido_IdPedido(idPedido);
    }

    @Override
    public DetallePedido guardar(DetallePedido d) {
        return repository.save(d);
    }

    @Override
    public void eliminar(int id) {
        repository.deleteById(id);
    }

    @Override
    public void eliminarPorPedido(int idPedido) {
        repository.deleteByPedido_IdPedido(idPedido);
    }
}
