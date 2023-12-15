package mate.academy.mapper;

import mate.academy.config.MapperConfig;
import mate.academy.dto.cartItems.CartItemResponseDto;
import mate.academy.dto.shoppingCart.ShoppingCartRequestDto;
import mate.academy.dto.shoppingCart.ShoppingCartResponseDto;
import mate.academy.model.CartItem;
import mate.academy.model.ShoppingCart;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {
    @Mapping(target = "cartItems", source = "cartItems")
    @Mapping(target = "userId", source = "user.id")
    ShoppingCartResponseDto toDto(ShoppingCart shoppingCart);

    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    ShoppingCart toModel(ShoppingCartRequestDto requestDto);

    @AfterMapping
    default void setBookTitle(@MappingTarget CartItemResponseDto cartItemResponseDto,
                              CartItem cartItem) {
        cartItemResponseDto.setBookTitle(cartItemResponseDto.getBookTitle());
    }

    @AfterMapping
    default void setBookId(@MappingTarget CartItemResponseDto cartItemResponseDto,
                           CartItem cartItem) {
        cartItemResponseDto.setBookId(cartItemResponseDto.getBookId());
    }
}
