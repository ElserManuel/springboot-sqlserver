package ap2.ElserManuelLazaroRosas.service;

import ap2.ElserManuelLazaroRosas.model.Proveedor;
import java.util.List;
import java.util.Optional;

public interface ProveedorService {
    List<Proveedor> listar();

    Optional<Proveedor> buscarPorId(int id);

    Proveedor guardar(Proveedor p);

    Proveedor actualizar(int id, Proveedor p);

    void eliminar(int id);

    Proveedor cambiarEstado(int id, Boolean activo);

    List<Proveedor> listarTodos();
}