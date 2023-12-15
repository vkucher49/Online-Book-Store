package mate.academy.dto.shoppingCart;

import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.Data;
import mate.academy.dto.cartItems.CartItemResponseDto;

@Data
public class ShoppingCartRequestDto {
    @NotNull
    private Long userId;
    private Set<CartItemResponseDto> cartItems;
}
