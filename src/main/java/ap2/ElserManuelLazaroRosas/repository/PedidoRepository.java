package ap2.ElserManuelLazaroRosas.repository;

import ap2.ElserManuelLazaroRosas.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
 
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    List<Pedido> findByCliente_IdClienteOrderByFechaPedidoDesc(Integer idCliente);
    List<Pedido> findByEstado(String estado);
    Optional<Pedido> findByNumeroPedido(String numeroPedido);
    boolean existsByNumeroPedido(String numeroPedido);
}
 