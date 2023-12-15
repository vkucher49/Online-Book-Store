package mate.academy.dto.cartitems;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CartItemRequestDto {
    @NotNull
    @Positive
    private Long bookId;
    @NotNull(message = "Enter quantity")
    @Min(value = 0, message = "Invalid quantity, it can't be negative number")
    private Integer quantity;
}
