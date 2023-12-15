package mate.academy.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BookQuantityDto {
    @NotNull(message = "Enter the quantity")
    @Min(value = 0, message = "Invalid quantity, it can't be negative number")
    private Integer quantity;
}
