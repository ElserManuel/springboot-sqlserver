package ap2.ElserManuelLazaroRosas.service;

import ap2.ElserManuelLazaroRosas.model.DetallePedido;
import java.util.List;
import java.util.Optional;

public interface DetallePedidoService {
    List<DetallePedido> listar();

    Optional<DetallePedido> buscarPorId(int id);

    List<DetallePedido> listarPorPedido(int idPedido);

    DetallePedido guardar(DetallePedido d);

    void eliminar(int id);

    void eliminarPorPedido(int idPedido);
}