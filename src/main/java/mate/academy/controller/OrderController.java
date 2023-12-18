package mate.academy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.order.OrderItemResponseDto;
import mate.academy.dto.order.OrderRequestDto;
import mate.academy.dto.order.OrderResponseDto;
import mate.academy.dto.order.OrderUpdateStatusRequestDto;
import mate.academy.model.User;
import mate.academy.service.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Order management", description = "Endpoints for Order")
@RequestMapping(name = "/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Create order", description = "Create new order")
    @PreAuthorize("hasRole('ROLE_USER')")
    public OrderResponseDto create(Authentication authentication,
                                   @RequestBody @Valid OrderRequestDto requestDto) {
        User user = (User) authentication.getPrincipal();
        return orderService.createUserOrder(user.getId(), requestDto);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get all orders", description = "Get all orders")
    public List<OrderResponseDto> findAllOrdersByUser(Authentication authentication,
                                                      Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return orderService.findAllOrdersByUser(user.getId(), pageable);
    }

    @PatchMapping("/{orderId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Update order status", description = "Update order status")
    public OrderResponseDto updateOrderStatus(@PathVariable Long orderId,
                                              @RequestBody @Valid OrderUpdateStatusRequestDto
                                                      orderUpdateStatusRequestDto) {
        return orderService.updateOrderStatus(orderId, orderUpdateStatusRequestDto);
    }

    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Get all order items", description = "Get all order items from order")
    public Set<OrderItemResponseDto> findAllOrderItemsByOrder(@PathVariable Long orderId) {
        return orderService.findAllOrderItems(orderId);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Find order by item", description = "Find order by item id")
    public OrderItemResponseDto findOrderByItemId(@PathVariable Long orderId,
                                                  @PathVariable Long itemId) {
        return orderService.findOrderItemByOrderId(orderId, itemId);
    }
}
