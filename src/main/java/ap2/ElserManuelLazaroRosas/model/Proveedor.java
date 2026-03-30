package ap2.ElserManuelLazaroRosas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Proveedor")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Proveedor de productos")
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor")
    @Schema(description = "ID único del proveedor", example = "1")
    private Integer idProveedor;

    @Column(name = "ruc", nullable = false, unique = true, length = 20)
    @Schema(description = "RUC del proveedor", example = "20100001111")
    private String ruc;

    @Column(name = "razon_social", nullable = false, length = 150)
    @Schema(description = "Razón social del proveedor", example = "Tech Distribuciones SAC")
    private String razonSocial;

    @Column(name = "contacto", length = 100)
    @Schema(description = "Nombre del contacto", example = "Juan Pérez")
    private String contacto;

    @Column(name = "telefono", length = 20)
    @Schema(description = "Teléfono del proveedor", example = "999111222")
    private String telefono;

    @Column(name = "email", length = 100)
    @Schema(description = "Email del proveedor", example = "ventas@techdist.com")
    private String email;

    @Column(name = "direccion", length = 255)
    @Schema(description = "Dirección del proveedor", example = "Av. Industrial 123, Lima")
    private String direccion;

    @Column(name = "activo", nullable = false)
    @Builder.Default
    @Schema(description = "Estado activo/inactivo", example = "true")
    private Boolean activo = true;

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    @Schema(description = "Fecha de creación del registro")
    private LocalDateTime fechaCreacion;

    @OneToMany(mappedBy = "proveedor", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Schema(hidden = true)
    private List<Producto> productos;
}