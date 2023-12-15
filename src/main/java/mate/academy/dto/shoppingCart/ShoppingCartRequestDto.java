package mate.academy.dto.shoppingCart;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import mate.academy.dto.cartItems.CartItemResponseDto;

import java.util.Set;

@Data
public class ShoppingCartRequestDto {
    @NotNull
    private Long userId;
    private Set<CartItemResponseDto> cartItems;
}
