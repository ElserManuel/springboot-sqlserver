package ap2.ElserManuelLazaroRosas.service;

import ap2.ElserManuelLazaroRosas.model.Categoria;
import java.util.List;
import java.util.Optional;

public interface CategoriaService {
    List<Categoria> listar();

    Optional<Categoria> buscarPorId(int id);

    Categoria guardar(Categoria c);

    Categoria actualizar(int id, Categoria c);

    void eliminar(int id);

    Categoria cambiarEstado(int id, Boolean activo);

    List<Categoria> listarTodos();
}
