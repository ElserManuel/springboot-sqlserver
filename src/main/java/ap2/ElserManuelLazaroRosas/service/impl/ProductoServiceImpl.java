package ap2.ElserManuelLazaroRosas.service.impl;

import ap2.ElserManuelLazaroRosas.model.Producto;
import ap2.ElserManuelLazaroRosas.repository.ProductoRepository;
import ap2.ElserManuelLazaroRosas.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository repository;

    @Override
    public List<Producto> listar() {
        return repository.findByActivoTrue();
    }

    @Override
    public Optional<Producto> buscarPorId(int id) {
        return repository.findById(id);
    }

    @Override
    public Producto guardar(Producto producto) {
        if (producto.getActivo() == null) {
            producto.setActivo(true);
        }
        return repository.save(producto);
    }

    @Override
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

    @Override
    public void eliminar(int id) {
        repository.findById(id).ifPresent(p -> {
            p.setActivo(false);
            repository.save(p);
        });
    }

    @Override
    public List<Producto> listarPorCategoria(int idCategoria) {
        return repository.findByCategoria_IdCategoriaAndActivoTrue(idCategoria);
    }

    @Override
    public Producto cambiarEstado(int id, Boolean activo) {
        return repository.findById(id).map(existing -> {
            existing.setActivo(activo);
            return repository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id));
    }

    @Override
    public List<Producto> listarTodos() {
        return repository.findAll();
    }
}
