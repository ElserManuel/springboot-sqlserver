package ap2.ElserManuelLazaroRosas.service.impl;

import ap2.ElserManuelLazaroRosas.model.Categoria;
import ap2.ElserManuelLazaroRosas.repository.CategoriaRepository;
import ap2.ElserManuelLazaroRosas.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository repository;

    @Override
    public List<Categoria> listar() {
        return repository.findByActivoTrue();
    }

    @Override
    public Optional<Categoria> buscarPorId(int id) {
        return repository.findById(id);
    }

    @Override
    public Categoria guardar(Categoria c) {
        if (c.getActivo() == null) {
            c.setActivo(true);
        }
        return repository.save(c);
    }

    @Override
    public Categoria actualizar(int id, Categoria c) {
        return repository.findById(id).map(existing -> {
            existing.setNombre(c.getNombre());
            existing.setDescripcion(c.getDescripcion());
            return repository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Categoria no encontrada: " + id));
    }

    @Override
    public void eliminar(int id) {
        repository.findById(id).ifPresent(c -> {
            c.setActivo(false);
            repository.save(c);
        });
    }

    @Override
    public Categoria cambiarEstado(int id, Boolean activo) {
        return repository.findById(id).map(existing -> {
            existing.setActivo(activo);
            return repository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Categoria no encontrada: " + id));
    }

    @Override
    public List<Categoria> listarTodos() {
        return repository.findAll();
    }
}
