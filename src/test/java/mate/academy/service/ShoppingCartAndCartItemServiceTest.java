package mate.academy.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import mate.academy.dto.cartitems.CartItemResponseDto;
import mate.academy.dto.shoppingcart.ShoppingCartResponseDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.ShoppingCartMapper;
import mate.academy.model.Book;
import mate.academy.model.CartItem;
import mate.academy.model.Role;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;
import mate.academy.repository.cartitem.CartItemRepository;
import mate.academy.repository.shoppingcart.ShoppingCartRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartAndCartItemServiceTest {
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;
    @Mock
    private UserService userService;
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;

    @DisplayName("Get shopping cart of user")
    @Test
    void getShoppingCartDto_ValidShoppingCartDto_Success() {
        // given
        ShoppingCart shoppingCart = getShoppingCart();
        ShoppingCartResponseDto expected = getShoppingCartResponseDto(shoppingCart);
        when(userService.getUser()).thenReturn(shoppingCart.getUser());
        when(shoppingCartRepository.findById(anyLong()))
                .thenReturn(Optional.of(shoppingCart));
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(expected);
        // when
        ShoppingCartResponseDto actual = shoppingCartService.getShoppingCartDto();
        // then
        Assertions.assertNotNull(actual);
        Assertions.assertNotNull(actual.getId());
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @DisplayName("Get shopping cart by valid user ID")
    void getShoppingCartByUserId_ValidUserId_ReturnShoppingCartDto() {
        // given
        Long userId = anyLong();
        ShoppingCart shoppingCart = getShoppingCart();
        ShoppingCartResponseDto expected = getShoppingCartResponseDto(shoppingCart);
        when(shoppingCartRepository.findById(userId)).thenReturn(Optional.of(shoppingCart));
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(expected);
        // when
        ShoppingCartResponseDto actual = shoppingCartService.getShoppingCartByUserId(userId);
        // then
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @DisplayName("Get shopping cart by invalid user ID")
    void getShoppingCartById_InvalidId_ThrowException() {
        // given
        Long invalidUserId = 100L;
        when(shoppingCartRepository.findById(invalidUserId))
                .thenReturn(Optional.empty());
        String expected = "Can't find shopping cart by ID: " + invalidUserId;
        // when
        EntityNotFoundException requestException =
                assertThrows(EntityNotFoundException.class,
                        () -> shoppingCartService.getShoppingCartByUserId(
                                invalidUserId));
        String actual = requestException.getMessage();
        // then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Delete cart item by valid ID")
    void deleteCartItem_ValidId_Success() {
        Long cartItemId = anyLong();
        CartItem cartItem = getCartItem();
        when(cartItemRepository.existsById(cartItemId)).thenReturn(true);
        assertDoesNotThrow(() -> shoppingCartService.deleteCartItem(cartItem.getId()));
    }

    private ShoppingCart getShoppingCart() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        shoppingCart.setUser(getUser());
        shoppingCart.setCartItems(Set.of());
        shoppingCart.setDeleted(false);
        return shoppingCart;
    }

    private User getUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("email");
        user.setPassword("password");
        user.setFirstName("first name");
        user.setLastName("last name");
        user.setShippingAddress("shopping cart address");
        user.setRoles(Set.of(getUserRole()));
        user.setDeleted(false);
        return user;
    }

    private Role getUserRole() {
        Role role = new Role();
        role.setId(1L);
        role.setRoleName(Role.RoleName.ROLE_USER);
        role.setDeleted(false);
        return role;
    }

    private CartItem getCartItem() {
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setShoppingCart(new ShoppingCart());
        cartItem.setBook(getBookA());
        cartItem.setQuantity(10);
        cartItem.setDeleted(false);
        return cartItem;
    }

    private Book getBookA() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Book A");
        book.setAuthor("Author A");
        book.setIsbn("123456");
        book.setPrice(BigDecimal.valueOf(50));
        book.setDescription("Description book");
        book.setCoverImage("http://example.com/bookA.jpg");
        book.setDeleted(false);
        return book;
    }

    private ShoppingCartResponseDto getShoppingCartResponseDto(ShoppingCart shoppingCart) {
        ShoppingCartResponseDto shoppingCartDto = new ShoppingCartResponseDto();
        shoppingCartDto.setId(shoppingCart.getId());
        shoppingCartDto.setUserId(shoppingCartDto.getUserId());
        shoppingCartDto.setCartItems(Set.of(new CartItemResponseDto()));
        return shoppingCartDto;
    }
}
