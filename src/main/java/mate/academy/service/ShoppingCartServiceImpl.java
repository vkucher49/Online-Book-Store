package mate.academy.service;

import lombok.RequiredArgsConstructor;
import mate.academy.dto.book.BookQuantityDto;
import mate.academy.dto.cartItems.CartItemRequestDto;
import mate.academy.dto.shoppingCart.ShoppingCartResponseDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.CartItemMapper;
import mate.academy.mapper.ShoppingCartMapper;
import mate.academy.model.Book;
import mate.academy.model.CartItem;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.repository.book.BookRepository;
import mate.academy.repository.cartItem.CartItemRepository;
import mate.academy.repository.shoppingCart.ShoppingCartRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;
    private final BookRepository bookRepository;
    private final UserService userService;

    @Override
    public ShoppingCartResponseDto getShoppingCartDto() {
        return shoppingCartMapper.toDto(getShoppingCartModel());
    }

    @Override
    public ShoppingCartResponseDto getShoppingCartByUserId(Long id) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find shopping cart by ID: " + id));
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartResponseDto addBook(CartItemRequestDto cartItemRequestDto) {
        ShoppingCart shoppingCart = getShoppingCartModel();
        Book book = bookRepository.findById(cartItemRequestDto.getBookId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by ID"));
        CartItem cartItem = cartItemMapper.toModel(cartItemRequestDto, book);
        cartItem.setShoppingCart(shoppingCart);
        CartItem saveCartItem = cartItemRepository.save(cartItem);
        shoppingCart.addCartItem(saveCartItem);
        return getShoppingCartDto();
    }

    @Override
    public ShoppingCartResponseDto updateBookQuantity(BookQuantityDto bookQuantityDto,
                                                      Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(
                () -> new EntityNotFoundException("Can't find cart item by ID: " + cartItemId));
        cartItem.setQuantity(bookQuantityDto.getQuantity());
        cartItemRepository.save(cartItem);
        return getShoppingCartDto();
    }

    @Override
    public void deleteCartItem(Long cartItemId) {
        if (!cartItemRepository.existsById(cartItemId)) {
            throw new EntityNotFoundException("Can't delete cart by ID: " + cartItemId);
        }

        cartItemRepository.deleteById(cartItemId);
    }

    private ShoppingCart getShoppingCartModel() {
        User user = userService.getUser();
        return shoppingCartRepository.findById(user.getId()).get();
    }
}
