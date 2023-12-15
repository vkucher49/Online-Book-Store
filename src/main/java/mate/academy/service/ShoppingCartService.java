package mate.academy.service;

import mate.academy.dto.book.BookQuantityDto;
import mate.academy.dto.cartitems.CartItemRequestDto;
import mate.academy.dto.shoppingcart.ShoppingCartResponseDto;

public interface ShoppingCartService {
    ShoppingCartResponseDto getShoppingCartDto();

    ShoppingCartResponseDto getShoppingCartByUserId(Long id);

    ShoppingCartResponseDto addBook(CartItemRequestDto cartItemRequestDto);

    ShoppingCartResponseDto updateBookQuantity(BookQuantityDto bookQuantityDto, Long cartItemId);

    void deleteCartItem(Long cartItemId);
}
