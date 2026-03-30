package ap2.ElserManuelLazaroRosas.rest;

import ap2.ElserManuelLazaroRosas.model.Proveedor;
import ap2.ElserManuelLazaroRosas.service.ProveedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
@RequiredArgsConstructor
@Tag(name = "Proveedores", description = "CRUD de proveedores")
public class ProveedorRest {

    private final ProveedorService service;

    @GetMapping
    @Operation(summary = "Listar todos los proveedores activos")
    public List<Proveedor> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar proveedor por ID")
    public ResponseEntity<Proveedor> buscar(@PathVariable int id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear nuevo proveedor")
    public Proveedor crear(@RequestBody Proveedor proveedor) {
        return service.guardar(proveedor);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar proveedor")
    public ResponseEntity<Proveedor> actualizar(@PathVariable int id, @RequestBody Proveedor proveedor) {
        try {
            return ResponseEntity.ok(service.actualizar(id, proveedor));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar (desactivar) proveedor")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Cambiar estado activo/inactivo del proveedor")
    public ResponseEntity<Proveedor> cambiarEstado(@PathVariable int id, @RequestParam String activo) {
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
    @Operation(summary = "Listar TODOS los proveedores (activos e inactivos)")
    public List<Proveedor> listarTodos() {
        return service.listarTodos();
    }

}
