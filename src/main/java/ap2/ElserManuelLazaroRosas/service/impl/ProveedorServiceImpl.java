package ap2.ElserManuelLazaroRosas.service.impl;

import ap2.ElserManuelLazaroRosas.model.Proveedor;
import ap2.ElserManuelLazaroRosas.repository.ProveedorRepository;
import ap2.ElserManuelLazaroRosas.service.ProveedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository repository;

    @Override
    public List<Proveedor> listar() {
        return repository.findByActivoTrue();
    }

    @Override
    public Optional<Proveedor> buscarPorId(int id) {
        return repository.findById(id);
    }

    @Override
    public Proveedor guardar(Proveedor p) {
        if (p.getActivo() == null) {
            p.setActivo(true);
        }
        return repository.save(p);
    }

    @Override
    public Proveedor actualizar(int id, Proveedor p) {
        return repository.findById(id).map(existing -> {
            existing.setRazonSocial(p.getRazonSocial());
            existing.setRuc(p.getRuc());
            existing.setContacto(p.getContacto());
            existing.setTelefono(p.getTelefono());
            existing.setEmail(p.getEmail());
            existing.setDireccion(p.getDireccion());
            return repository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Proveedor no encontrado: " + id));
    }

    @Override
    public void eliminar(int id) {
        repository.findById(id).ifPresent(p -> {
            p.setActivo(false);
            repository.save(p);
        });
    }

    @Override
    public Proveedor cambiarEstado(int id, Boolean activo) {
        return repository.findById(id).map(existing -> {
            existing.setActivo(activo);
            return repository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Proveedor no encontrado: " + id));
    }

    @Override
    public List<Proveedor> listarTodos() {
        return repository.findAll();
    }
}
