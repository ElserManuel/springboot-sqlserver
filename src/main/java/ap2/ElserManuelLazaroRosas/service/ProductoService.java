package ap2.ElserManuelLazaroRosas.service;

import ap2.ElserManuelLazaroRosas.model.Producto;
import ap2.ElserManuelLazaroRosas.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
 
@Service
@RequiredArgsConstructor
public class ProductoService {
 
    private final ProductoRepository repository;
 
    public List<Producto> listar()                  { return repository.findByActivoTrue(); }
    public Optional<Producto> buscarPorId(int id)   { return repository.findById(id); }
        public Producto guardar(Producto producto) {
            if (producto.getActivo() == null) {
                producto.setActivo(true);
            }
            return repository.save(producto);
        }
 
    public Producto actualizar(int id, Producto p) {
        return repository.findById(id).map(existing -> {
            existing.setNombre(p.getNombre());
            existing.setCodigo(p.getCodigo());
            existing.setDescripcion(p.getDescripcion());
            existing.setPrecioCompra(p.getPrecioCompra());
            existing.setPrecioVenta(p.getPrecioVenta());
            existing.setStock(p.getStock());
            existing.setStockMinimo(p.getStockMinimo());
            existing.setUnidadMedida(p.getUnidadMedida());
            existing.setCategoria(p.getCategoria());
            existing.setProveedor(p.getProveedor());
            return repository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id));
    }
 
    public void eliminar(int id) {
        repository.findById(id).ifPresent(p -> {
            p.setActivo(false);
            repository.save(p);
        });
    }
 
    public List<Producto> listarPorCategoria(int idCategoria) {
        return repository.findByCategoria_IdCategoriaAndActivoTrue(idCategoria);
    }

    public Producto cambiarEstado(int id, Boolean activo) {
        return repository.findById(id).map(existing -> {
            existing.setActivo(activo);
            return repository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id));
    }

    public List<Producto> listarTodos() {
        return repository.findAll();
    }
}