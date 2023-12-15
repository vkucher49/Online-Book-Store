package mate.academy.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import mate.academy.model.Order;

@Data
public class OrderUpdateStatusRequestDto {
    @NotNull(message = "Fill in the status")
    private Order.Status status;
}
