package ap2.ElserManuelLazaroRosas.repository;

import ap2.ElserManuelLazaroRosas.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
 
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findByActivoTrue();
    Optional<Producto> findByCodigo(String codigo);
    List<Producto> findByCategoria_IdCategoriaAndActivoTrue(Integer idCategoria);
    boolean existsByCodigo(String codigo);
}