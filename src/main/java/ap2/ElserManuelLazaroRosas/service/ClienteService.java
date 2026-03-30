package ap2.ElserManuelLazaroRosas.service;

import ap2.ElserManuelLazaroRosas.model.Cliente;
import java.util.List;
import java.util.Optional;

public interface ClienteService {
    List<Cliente> listar();

    Optional<Cliente> buscarPorId(int id);

    Cliente guardar(Cliente c);

    Cliente actualizar(int id, Cliente c);

    void eliminar(int id);

    Cliente cambiarEstado(int id, Boolean activo);

    List<Cliente> listarTodos();
}

