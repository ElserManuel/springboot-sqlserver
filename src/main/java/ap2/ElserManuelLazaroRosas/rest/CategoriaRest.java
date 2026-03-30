package ap2.ElserManuelLazaroRosas.rest;

import ap2.ElserManuelLazaroRosas.model.Categoria;
import ap2.ElserManuelLazaroRosas.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
@Tag(name = "Categorias", description = "CRUD de categorías")
public class CategoriaRest {

    private final CategoriaService service;

    @GetMapping
    @Operation(summary = "Listar todas las categorías activas")
    public List<Categoria> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar categoría por ID")
    public ResponseEntity<Categoria> buscar(@PathVariable int id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear nueva categoría")
    public Categoria crear(@RequestBody Categoria categoria) {
        return service.guardar(categoria);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar categoría")
    public ResponseEntity<Categoria> actualizar(@PathVariable int id, @RequestBody Categoria categoria) {
        try {
            return ResponseEntity.ok(service.actualizar(id, categoria));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar (desactivar) categoría")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Cambiar estado activo/inactivo de la categoría")
    public ResponseEntity<Categoria> cambiarEstado(@PathVariable int id, @RequestParam String activo) {
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

    @GetMapping("/todos/all")
    @Operation(summary = "Listar TODAS las categorías (activas e inactivas)")
    public List<Categoria> listarTodos() {
        return service.listarTodos();
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

}
