package ap2.ElserManuelLazaroRosas.service;

import ap2.ElserManuelLazaroRosas.model.Producto;
import java.util.List;
import java.util.Optional;

public interface ProductoService {
    List<Producto> listar();

    Optional<Producto> buscarPorId(int id);

    Producto guardar(Producto producto);

    Producto actualizar(int id, Producto p);

    void eliminar(int id);

    List<Producto> listarPorCategoria(int idCategoria);

    Producto cambiarEstado(int id, Boolean activo);

    List<Producto> listarTodos();
}