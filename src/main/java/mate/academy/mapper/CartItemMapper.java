package mate.academy.mapper;

import mate.academy.config.MapperConfig;
import mate.academy.dto.book.BookQuantityDto;
import mate.academy.dto.cartitems.CartItemRequestDto;
import mate.academy.model.Book;
import mate.academy.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    BookQuantityDto toDto(CartItem cartItem);

    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "shoppingCart", ignore = true)
    CartItem toModel(CartItemRequestDto requestDto, Book book);

    BookQuantityDto toBookQuantityDto(CartItem cartItem);
}
