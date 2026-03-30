package ap2.ElserManuelLazaroRosas.service.impl;

import ap2.ElserManuelLazaroRosas.model.Cliente;
import ap2.ElserManuelLazaroRosas.repository.ClienteRepository;
import ap2.ElserManuelLazaroRosas.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository repository;

    @Override
    public List<Cliente> listar() {
        return repository.findByActivoTrue();
    }

    @Override
    public Optional<Cliente> buscarPorId(int id) {
        return repository.findById(id);
    }

    @Override
    public Cliente guardar(Cliente c) {
        if (c.getActivo() == null) {
            c.setActivo(true);
        }
        return repository.save(c);
    }

    @Override
    public Cliente actualizar(int id, Cliente c) {
        return repository.findById(id).map(existing -> {
            existing.setNombre(c.getNombre());
            existing.setApellido(c.getApellido());
            existing.setTipoDoc(c.getTipoDoc());
            existing.setNumDoc(c.getNumDoc());
            existing.setEmail(c.getEmail());
            existing.setTelefono(c.getTelefono());
            existing.setDireccion(c.getDireccion());
            existing.setCiudad(c.getCiudad());
            return repository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + id));
    }

    @Override
    public void eliminar(int id) {
        repository.findById(id).ifPresent(c -> {
            c.setActivo(false);
            repository.save(c);
        });
    }

    @Override
    public Cliente cambiarEstado(int id, Boolean activo) {
        return repository.findById(id).map(existing -> {
            existing.setActivo(activo);
            return repository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + id));
    }

    @Override
    public List<Cliente> listarTodos() {
        return repository.findAll();
    }
}
