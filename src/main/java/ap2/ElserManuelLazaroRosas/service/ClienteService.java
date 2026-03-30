package ap2.ElserManuelLazaroRosas.service;
 
import ap2.ElserManuelLazaroRosas.model.Cliente;
import ap2.ElserManuelLazaroRosas.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
 
@Service
@RequiredArgsConstructor
public class ClienteService {
 
    private final ClienteRepository repository;
 
    public List<Cliente> listar()                  { return repository.findByActivoTrue(); }
    public Optional<Cliente> buscarPorId(int id)   { return repository.findById(id); }
    public Cliente guardar(Cliente c) {
        if (c.getActivo() == null) {
            c.setActivo(true);
        }
        return repository.save(c);
    }
 
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
 
    public void eliminar(int id) {
        repository.findById(id).ifPresent(c -> {
            c.setActivo(false);
            repository.save(c);
        });
    }

    public Cliente cambiarEstado(int id, Boolean activo) {
        return repository.findById(id).map(existing -> {
            existing.setActivo(activo);
            return repository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Cliente no encontrado: " + id));
    }

    public List<Cliente> listarTodos() {
        return repository.findAll();
    }
}
 