package ap2.ElserManuelLazaroRosas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Producto")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Producto del catálogo")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    @Schema(description = "ID único del producto", example = "1")
    private Integer idProducto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Schema(description = "Categoría del producto")
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proveedor")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Schema(description = "Proveedor del producto")
    private Proveedor proveedor;

    @Column(name = "codigo", nullable = false, unique = true, length = 50)
    @Schema(description = "Código único del producto", example = "ELEC-001")
    private String codigo;

    @Column(name = "nombre", nullable = false, length = 150)
    @Schema(description = "Nombre del producto", example = "Laptop HP 15 pulgadas")
    private String nombre;

    @Column(name = "descripcion", length = 500)
    @Schema(description = "Descripción del producto")
    private String descripcion;

    @Column(name = "precio_compra", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    @Schema(description = "Precio de compra", example = "2500.00")
    private BigDecimal precioCompra = BigDecimal.ZERO;

    @Column(name = "precio_venta", nullable = false, precision = 10, scale = 2)
    @Schema(description = "Precio de venta", example = "3200.00")
    private BigDecimal precioVenta;

    @Column(name = "stock", nullable = false)
    @Builder.Default
    @Schema(description = "Cantidad en stock", example = "10")
    private Integer stock = 0;

    @Column(name = "stock_minimo", nullable = false)
    @Builder.Default
    @Schema(description = "Stock mínimo permitido", example = "5")
    private Integer stockMinimo = 5;

    @Column(name = "unidad_medida", nullable = false, length = 30)
    @Builder.Default
    @Schema(description = "Unidad de medida", example = "UNIDAD")
    private String unidadMedida = "UNIDAD";

    @Column(name = "imagen_url", length = 300)
    @Schema(description = "URL de la imagen del producto")
    private String imagenUrl;

    @Column(name = "activo", nullable = false)
    @Builder.Default
    @Schema(description = "Estado activo/inactivo", example = "true")
    private Boolean activo = true;

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    @Schema(description = "Fecha de creación")
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fecha_actualizacion")
    @Schema(description = "Fecha de última actualización")
    private LocalDateTime fechaActualizacion;

    @OneToMany(mappedBy = "producto", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Schema(hidden = true)
    private List<DetallePedido> detalles;
}