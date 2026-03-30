package ap2.ElserManuelLazaroRosas.rest;

import ap2.ElserManuelLazaroRosas.model.Producto;
import ap2.ElserManuelLazaroRosas.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Tag(name = "Productos", description = "CRUD de productos")
public class ProductoRest {

    private final ProductoService service;

    @GetMapping
    @Operation(summary = "Listar todos los productos activos")
    public List<Producto> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar producto por ID")
    public ResponseEntity<Producto> buscar(@PathVariable int id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/categoria/{idCategoria}")
    @Operation(summary = "Listar productos por categoría")
    public List<Producto> listarPorCategoria(@PathVariable int idCategoria) {
        return service.listarPorCategoria(idCategoria);
    }

    @PostMapping
    @Operation(summary = "Crear nuevo producto")
    public Producto crear(@RequestBody Producto producto) {
        return service.guardar(producto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto")
    public ResponseEntity<Producto> actualizar(@PathVariable int id, @RequestBody Producto producto) {
        try {
            return ResponseEntity.ok(service.actualizar(id, producto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar (desactivar) producto")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Cambiar estado activo/inactivo del producto")
    public ResponseEntity<Producto> cambiarEstado(@PathVariable int id, @RequestParam String activo) {
        Boolean estado = parseEstado(activo);
        if (estado == null) {
            return ResponseEntity.badRequest().build();
        }
        try {
            return ResponseEntity.ok(service.cambiarEstado(id, estado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private Boolean parseEstado(String valor) {
        if (valor == null) {
            return null;
        }
        String normalized = valor.trim().toLowerCase();
        if ("true".equals(normalized) || "1".equals(normalized)) {
            return true;
        }
        if ("false".equals(normalized) || "0".equals(normalized)) {
            return false;
        }
        return null;
    }

    @GetMapping("/todos/all")
    @Operation(summary = "Listar TODOS los productos (activos e inactivos)")
    public List<Producto> listarTodos() {
        return service.listarTodos();
    }

}
