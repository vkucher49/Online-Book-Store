package mate.academy.dto.shoppingCart;

import lombok.Data;
import mate.academy.dto.cartItems.CartItemResponseDto;

import java.util.Set;

@Data
public class ShoppingCartResponseDto {
    private Long id;
    private Long userId;
    private Set<CartItemResponseDto> cartItems;
}
