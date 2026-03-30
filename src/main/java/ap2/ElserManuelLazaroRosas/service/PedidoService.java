package ap2.ElserManuelLazaroRosas.service;

import ap2.ElserManuelLazaroRosas.model.Pedido;
import java.util.List;
import java.util.Optional;

public interface PedidoService {
    List<Pedido> listar();

    Optional<Pedido> buscarPorId(int id);

    Pedido guardar(Pedido p);

    Pedido actualizar(int id, Pedido p);

    void eliminar(int id);

    List<Pedido> listarPorCliente(int idCliente);

    List<Pedido> listarPorEstado(String estado);
}