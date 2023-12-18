package mate.academy.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.order.OrderItemResponseDto;
import mate.academy.dto.order.OrderRequestDto;
import mate.academy.dto.order.OrderResponseDto;
import mate.academy.dto.order.OrderUpdateStatusRequestDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.OrderItemMapper;
import mate.academy.mapper.OrderMapper;
import mate.academy.model.CartItem;
import mate.academy.model.Order;
import mate.academy.model.OrderItem;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.repository.order.OrderItemRepository;
import mate.academy.repository.order.OrderRepository;
import mate.academy.repository.shoppingcart.ShoppingCartRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final UserService userService;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    public OrderResponseDto createUserOrder(Long userId, OrderRequestDto requestDto) {
        User user = userService.getUser();
        ShoppingCart shoppingCart = shoppingCartRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("Can't find shopping cart by ID: " + userId));
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        if (shoppingCart.getCartItems().isEmpty()) {
            throw new EntityNotFoundException("Your shopping cart is empty");
        }
        Order order = createOrderByUser(user);
        Set<OrderItem> orderItems = collectOrderItems(order, cartItems);
        order.setTotal(calculatePrice(orderItems));
        order.setOrderItems(orderItems);
        shoppingCart.setCartItems(Collections.emptySet());
        Order saveOrder = orderRepository.save(order);
        return orderMapper.toDto(saveOrder);
    }

    @Override
    public List<OrderResponseDto> findAllOrdersByUser(Long userId, Pageable pageable) {
        return orderRepository.findAll(pageable)
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public OrderResponseDto updateOrderStatus(Long orderId,
                                              OrderUpdateStatusRequestDto
                                                      orderUpdateStatusRequestDto) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Can't find order by ID: " + orderId));
        order.setStatus(orderUpdateStatusRequestDto.getStatus());
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public Set<OrderItemResponseDto> findAllOrderItems(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Can't find order by ID: " + orderId));
        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public OrderItemResponseDto findOrderItemByOrderId(Long orderId, Long itemId) {
        OrderItem orderItem = orderItemRepository.findOrderItemByOrderIdAndId(orderId, itemId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find order by id: " + orderId + " and item by id: " + itemId));
        return orderItemMapper.toDto(orderItem);
    }

    private Set<OrderItem> collectOrderItems(Order order, Set<CartItem> cartItems) {
        Set<OrderItem> orderItems = new HashSet<>();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getBook().getPrice());
            orderItems.add(orderItem);
        }
        return orderItems;
    }

    private BigDecimal calculatePrice(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem -> BigDecimal.valueOf(orderItem.getQuantity())
                        .multiply(orderItem.getPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Order createOrderByUser(User user) {
        Order order = new Order();
        order.setStatus(Order.Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(user.getShippingAddress());
        return order;
    }
}
