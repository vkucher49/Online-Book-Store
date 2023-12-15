package mate.academy.service;

import java.util.List;
import java.util.Set;
import mate.academy.dto.order.OrderItemResponseDto;
import mate.academy.dto.order.OrderRequestDto;
import mate.academy.dto.order.OrderResponseDto;
import mate.academy.dto.order.OrderUpdateStatusRequestDto;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto createUserOrder(Long userId, OrderRequestDto requestDto);

    List<OrderResponseDto> findAllOrdersByUser(Long userId, Pageable pageable);

    OrderResponseDto updateOrderStatus(Long orderId,
                                       OrderUpdateStatusRequestDto orderUpdateStatusRequestDto);

    Set<OrderItemResponseDto> findAllOrderItems(Long orderId);

    OrderItemResponseDto findOrderItemByOrderId(Long orderId, Long itemId);
}
