package mate.academy.mapper;

import mate.academy.config.MapperConfig;
import mate.academy.dto.order.OrderItemResponseDto;
import mate.academy.dto.order.OrderRequestDto;
import mate.academy.dto.order.OrderResponseDto;
import mate.academy.model.Order;
import mate.academy.model.OrderItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    @Mapping(target = "userId", source = "order.user.id")
    OrderResponseDto toDto(Order order);

    Order toModel(OrderRequestDto requestDto);

    @AfterMapping
    default void setBookId(@MappingTarget OrderItemResponseDto responseDto, OrderItem orderItem) {
        responseDto.setBookId(orderItem.getBook().getId());
    }
}
