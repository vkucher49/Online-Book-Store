package mate.academy.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class OrderItemRequestDto {
    @NotNull
    @Positive
    private Long bookId;
    @NotNull(message = "Enter the quantity")
    @Min(value = 0, message = "Invalid quantity, it can't be negative number")
    private Integer quantity;
}
