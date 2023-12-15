package mate.academy.dto.shoppingcart;

import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.Data;
import mate.academy.dto.cartitems.CartItemResponseDto;

@Data
public class ShoppingCartRequestDto {
    @NotNull
    private Long userId;
    private Set<CartItemResponseDto> cartItems;
}
