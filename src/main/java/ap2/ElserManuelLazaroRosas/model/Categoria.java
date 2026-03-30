package ap2.ElserManuelLazaroRosas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Categoria")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Categoría de productos")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    @Schema(description = "ID único de la categoría", example = "1")
    private Integer idCategoria;

    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    @Schema(description = "Nombre de la categoría", example = "Electrónica")
    private String nombre;

    @Column(name = "descripcion", length = 255)
    @Schema(description = "Descripción de la categoría", example = "Dispositivos electrónicos")
    private String descripcion;

    @Column(name = "activo", nullable = false)
    @Builder.Default
    @Schema(description = "Estado activo/inactivo", example = "true")
    private Boolean activo = true;

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    @Schema(description = "Fecha de creación del registro")
    private LocalDateTime fechaCreacion;

    @OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Schema(hidden = true)
    private List<Producto> productos;
}