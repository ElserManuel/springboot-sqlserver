package ap2.ElserManuelLazaroRosas.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Cliente")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Cliente del sistema")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    @Schema(description = "ID único del cliente", example = "1")
    private Integer idCliente;

    @Column(name = "tipo_doc", nullable = false, length = 10)
    @Builder.Default
    @Schema(description = "Tipo de documento", example = "DNI",
            allowableValues = {"DNI", "RUC", "CE", "PASAPORTE"})
    private String tipoDoc = "DNI";

    @Column(name = "num_doc", nullable = false, unique = true, length = 20)
    @Schema(description = "Número de documento", example = "12345678")
    private String numDoc;

    @Column(name = "nombre", nullable = false, length = 100)
    @Schema(description = "Nombre del cliente", example = "Luis")
    private String nombre;

    @Column(name = "apellido", length = 100)
    @Schema(description = "Apellido del cliente", example = "Gómez Rivas")
    private String apellido;

    @Column(name = "email", length = 100)
    @Schema(description = "Email del cliente", example = "luis.gomez@email.com")
    private String email;

    @Column(name = "telefono", length = 20)
    @Schema(description = "Teléfono del cliente", example = "987654321")
    private String telefono;

    @Column(name = "direccion", length = 255)
    @Schema(description = "Dirección del cliente", example = "Av. Lima 456")
    private String direccion;

    @Column(name = "ciudad", length = 80)
    @Schema(description = "Ciudad del cliente", example = "Lima")
    private String ciudad;

    @Column(name = "activo", nullable = false)
    @Builder.Default
    @Schema(description = "Estado activo/inactivo", example = "true")
    private Boolean activo = true;

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    @Schema(description = "Fecha de creación del registro")
    private LocalDateTime fechaCreacion;

    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Schema(hidden = true)
    private List<Pedido> pedidos;
}