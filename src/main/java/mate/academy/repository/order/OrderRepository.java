package mate.academy.repository.order;

import java.util.Set;
import mate.academy.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Set<Order> findAllByUserEmail(String email);
}
