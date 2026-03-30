package ap2.ElserManuelLazaroRosas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Pedido")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Pedido / Venta")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    @Schema(description = "ID único del pedido", example = "1")
    private Integer idPedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Schema(description = "Cliente que realizó el pedido")
    private Cliente cliente;

    @Column(name = "numero_pedido", nullable = false, unique = true, length = 20)
    @Schema(description = "Número de pedido", example = "PED-0001")
    private String numeroPedido;

    @Column(name = "fecha_pedido", nullable = false)
    @Schema(description = "Fecha y hora del pedido")
    private LocalDateTime fechaPedido;

    @Column(name = "estado", nullable = false, length = 20)
    @Builder.Default
    @Schema(description = "Estado del pedido", example = "PENDIENTE",
            allowableValues = {"PENDIENTE", "CONFIRMADO", "ENVIADO", "ENTREGADO", "CANCELADO"})
    private String estado = "PENDIENTE";

    @Column(name = "subtotal", nullable = false, precision = 12, scale = 2)
    @Builder.Default
    @Schema(description = "Subtotal antes de IGV", example = "3319.80")
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "descuento", nullable = false, precision = 12, scale = 2)
    @Builder.Default
    @Schema(description = "Descuento aplicado", example = "0.00")
    private BigDecimal descuento = BigDecimal.ZERO;

    @Column(name = "igv", nullable = false, precision = 12, scale = 2)
    @Builder.Default
    @Schema(description = "IGV (18%)", example = "597.56")
    private BigDecimal igv = BigDecimal.ZERO;

    @Column(name = "total", nullable = false, precision = 12, scale = 2)
    @Builder.Default
    @Schema(description = "Total a pagar", example = "3917.36")
    private BigDecimal total = BigDecimal.ZERO;

    @Column(name = "metodo_pago", length = 30)
    @Schema(description = "Método de pago", example = "EFECTIVO",
            allowableValues = {"EFECTIVO", "TARJETA", "TRANSFERENCIA"})
    private String metodoPago;

    @Column(name = "observacion", length = 500)
    @Schema(description = "Observaciones del pedido")
    private String observacion;

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    @Schema(description = "Fecha de creación del registro")
    private LocalDateTime fechaCreacion;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
        @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Schema(description = "Líneas del pedido")
    private List<DetallePedido> detalles;
}