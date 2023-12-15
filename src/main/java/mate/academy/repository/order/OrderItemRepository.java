package mate.academy.repository.order;

import java.util.Optional;
import java.util.Set;
import mate.academy.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Set<OrderItem> findAllByOrderId(Long orderId);

    Optional<OrderItem> findOrderItemByOrderIdAndId(Long orderId, Long id);
}
