package ap2.ElserManuelLazaroRosas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "DetallePedido")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Línea de detalle de un pedido")
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    @Schema(description = "ID único del detalle", example = "1")
    private Integer idDetalle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", nullable = false)
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Schema(description = "Pedido al que pertenece")
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_producto", nullable = false)
    @Schema(description = "Producto del detalle")
    private Producto producto;

    @Column(name = "cantidad", nullable = false)
    @Schema(description = "Cantidad solicitada", example = "2")
    private Integer cantidad;

    @Column(name = "precio_unit", nullable = false, precision = 10, scale = 2)
    @Schema(description = "Precio unitario al momento de la venta", example = "59.90")
    private BigDecimal precioUnit;

    @Column(name = "descuento", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    @Schema(description = "Descuento en esta línea", example = "0.00")
    private BigDecimal descuento = BigDecimal.ZERO;

    @Column(name = "subtotal", insertable = false, updatable = false, precision = 10, scale = 2)
    @Schema(description = "Subtotal calculado (cantidad × precio - descuento)", example = "119.80")
    private BigDecimal subtotal;
}