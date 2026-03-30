package ap2.ElserManuelLazaroRosas.repository;

import ap2.ElserManuelLazaroRosas.model.Categoria;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    List<Categoria> findByActivoTrue();
    boolean existsByNombre(String nombre);
}