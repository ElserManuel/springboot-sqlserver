package ap2.ElserManuelLazaroRosas.repository;

import ap2.ElserManuelLazaroRosas.model.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
 
@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Integer> {
    List<DetallePedido> findByPedido_IdPedido(Integer idPedido);
    void deleteByPedido_IdPedido(Integer idPedido);
}