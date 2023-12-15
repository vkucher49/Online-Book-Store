package mate.academy.dto.shoppingCart;

import java.util.Set;
import lombok.Data;
import mate.academy.dto.cartItems.CartItemResponseDto;

@Data
public class ShoppingCartResponseDto {
    private Long id;
    private Long userId;
    private Set<CartItemResponseDto> cartItems;
}
