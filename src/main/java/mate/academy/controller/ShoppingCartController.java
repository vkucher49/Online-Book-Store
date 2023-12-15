package mate.academy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.book.BookQuantityDto;
import mate.academy.dto.cartItems.CartItemRequestDto;
import mate.academy.dto.shoppingCart.ShoppingCartResponseDto;
import mate.academy.service.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Shopping Cart management", description = "Endpoints for Shopping Cart")
@RequiredArgsConstructor
@RequestMapping(value = "/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @GetMapping
    @Operation(summary = "Get user's shopping cart")
    public ShoppingCartResponseDto getCart() {
        return shoppingCartService.getShoppingCartDto();
    }

    @PostMapping
    @Operation(summary = "Add book to the shopping cart")
    public ShoppingCartResponseDto add(@RequestBody @Valid CartItemRequestDto requestDto) {
        return shoppingCartService.addBook(requestDto);
    }

    @PutMapping("/cart-items/{cartItemId}")
    @Operation(summary = "Update quantity of the books in the shopping cart")
    public ShoppingCartResponseDto update(@PathVariable Long cartItemId,
                                          @RequestBody @Valid BookQuantityDto quantityDto) {
        return shoppingCartService.updateBookQuantity(quantityDto, cartItemId);
    }

    @DeleteMapping("/cart-items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete cart item from the shopping cart")
    public void delete(@PathVariable Long cartItemId) {
        shoppingCartService.deleteCartItem(cartItemId);
    }
}
