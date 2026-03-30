package ap2.ElserManuelLazaroRosas.rest;

import ap2.ElserManuelLazaroRosas.model.DetallePedido;
import ap2.ElserManuelLazaroRosas.service.DetallePedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/detalles")
@RequiredArgsConstructor
@Tag(name = "Detalle Pedido", description = "CRUD de detalles de pedido")
public class DetallePedidoRest {

    private final DetallePedidoService service;

    @GetMapping
    @Operation(summary = "Listar todos los detalles")
    public List<DetallePedido> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar detalle por ID")
    public ResponseEntity<DetallePedido> buscar(@PathVariable int id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/pedido/{idPedido}")
    @Operation(summary = "Listar detalles por pedido")
    public List<DetallePedido> listarPorPedido(@PathVariable int idPedido) {
        return service.listarPorPedido(idPedido);
    }

    @PostMapping
    @Operation(summary = "Agregar detalle a pedido")
    public DetallePedido crear(@RequestBody DetallePedido detalle) {
        return service.guardar(detalle);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar detalle")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
