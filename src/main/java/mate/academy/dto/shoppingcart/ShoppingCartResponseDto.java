package mate.academy.dto.shoppingcart;

import java.util.Set;
import lombok.Data;
import mate.academy.dto.cartitems.CartItemResponseDto;

@Data
public class ShoppingCartResponseDto {
    private Long id;
    private Long userId;
    private Set<CartItemResponseDto> cartItems;
}
