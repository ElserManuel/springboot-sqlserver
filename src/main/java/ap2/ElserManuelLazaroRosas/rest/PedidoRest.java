package ap2.ElserManuelLazaroRosas.rest;

import ap2.ElserManuelLazaroRosas.model.Pedido;
import ap2.ElserManuelLazaroRosas.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "CRUD de pedidos")
public class PedidoRest {

    private final PedidoService service;

    @GetMapping
    @Operation(summary = "Listar todos los pedidos")
    public List<Pedido> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID")
    public ResponseEntity<Pedido> buscar(@PathVariable int id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{idCliente}")
    @Operation(summary = "Listar pedidos por cliente")
    public List<Pedido> listarPorCliente(@PathVariable int idCliente) {
        return service.listarPorCliente(idCliente);
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Listar pedidos por estado")
    public List<Pedido> listarPorEstado(@PathVariable String estado) {
        return service.listarPorEstado(estado);
    }

    @PostMapping
    @Operation(summary = "Crear nuevo pedido")
    public Pedido crear(@RequestBody Pedido pedido) {
        return service.guardar(pedido);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar pedido")
    public ResponseEntity<Pedido> actualizar(@PathVariable int id, @RequestBody Pedido pedido) {
        try {
            return ResponseEntity.ok(service.actualizar(id, pedido));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar pedido")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
