package ap2.ElserManuelLazaroRosas.rest;

import ap2.ElserManuelLazaroRosas.model.Cliente;
import ap2.ElserManuelLazaroRosas.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "CRUD de clientes")
public class ClienteRest {

    private final ClienteService service;

    @GetMapping
    @Operation(summary = "Listar todos los clientes activos")
    public List<Cliente> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID")
    public ResponseEntity<Cliente> buscar(@PathVariable int id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear nuevo cliente")
    public Cliente crear(@RequestBody Cliente cliente) {
        return service.guardar(cliente);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cliente")
    public ResponseEntity<Cliente> actualizar(@PathVariable int id, @RequestBody Cliente cliente) {
        try {
            return ResponseEntity.ok(service.actualizar(id, cliente));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar (desactivar) cliente")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Cambiar estado activo/inactivo del cliente")
    public ResponseEntity<Cliente> cambiarEstado(@PathVariable int id, @RequestParam String activo) {
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
    @Operation(summary = "Listar TODOS los clientes (activos e inactivos)")
    public List<Cliente> listarTodos() {
        return service.listarTodos();
    }

}
