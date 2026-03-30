package ap2.ElserManuelLazaroRosas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import ap2.ElserManuelLazaroRosas.model.Categoria;
import ap2.ElserManuelLazaroRosas.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository repository;

    public List<Categoria> listar() {
        return repository.findByActivoTrue();
    }

    public Optional<Categoria> buscarPorId(int id) {
        return repository.findById(id);
    }

    public Categoria guardar(Categoria c) {
        if (c.getActivo() == null) {
            c.setActivo(true);
        }
        return repository.save(c);
    }

    public Categoria actualizar(int id, Categoria c) {
        return repository.findById(id).map(existing -> {
            existing.setNombre(c.getNombre());
            existing.setDescripcion(c.getDescripcion());
            return repository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Categoria no encontrada: " + id));
    }

    public void eliminar(int id) {
        repository.findById(id).ifPresent(c -> {
            c.setActivo(false);
            repository.save(c);
        });
    }

    public Categoria cambiarEstado(int id, Boolean activo) {
        return repository.findById(id).map(existing -> {
            existing.setActivo(activo);
            return repository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Categoria no encontrada: " + id));
    }

    public List<Categoria> listarTodos() {
        return repository.findAll();
    }

}
