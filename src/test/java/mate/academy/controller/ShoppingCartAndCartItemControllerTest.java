package mate.academy.controller;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.dto.book.BookQuantityDto;
import mate.academy.dto.cartitems.CartItemRequestDto;
import mate.academy.dto.cartitems.CartItemResponseDto;
import mate.academy.dto.shoppingcart.ShoppingCartResponseDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShoppingCartAndCartItemControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext,
            @Autowired DataSource dataSource) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection, new ClassPathResource(
                    "data/books/add-data-to-cart.sql"
            ));
        }
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource
    ) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection, new ClassPathResource(
                    "data/books/remove-all-from-cart.sql"
            ));
        }
    }

    @Test
    @WithMockUser(username = "user@gmail.com")
    @DisplayName("Get shopping cart by user")
    void getCart_Ok() throws Exception {
        // given
        ShoppingCartResponseDto expected = getCartResponseDto();
        // when
        MvcResult mvcResult = mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andReturn();
        // then
        ShoppingCartResponseDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), ShoppingCartResponseDto.class);
        assertNotNull(actual);
        assertNotEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "user@gmail.com")
    @DisplayName("Add book to cart by wrong id")
    void addBookToShoppingCart_Ok() throws Exception {
        // given
        CartItemRequestDto requestDto = new CartItemRequestDto();
        requestDto.setBookId(2L);
        requestDto.setQuantity(50);
        ShoppingCartResponseDto expected = getCartResponseDto();
        String request = objectMapper.writeValueAsString(requestDto);
        // when
        MvcResult mvcResult = mockMvc.perform(post("/cart")
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        // then
        ShoppingCartResponseDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), ShoppingCartResponseDto.class);
        assertNotNull(actual);
        assertNotEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "user@gmail.com")
    @DisplayName("Update quantity of books")
    void updateQuantityOfBook_Ok() throws Exception {
        // given
        int updatedQuantity = 25;
        ShoppingCartResponseDto expected = getCartResponseDto();
        BookQuantityDto requestDto = new BookQuantityDto();
        requestDto.setQuantity(updatedQuantity);
        String request = objectMapper.writeValueAsString(requestDto);
        // when
        MvcResult mvcResult = mockMvc.perform(put("/cart/cart-items/{cartItemId}", 1L)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        // then
        ShoppingCartResponseDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(), ShoppingCartResponseDto.class);
        assertNotNull(actual);
        assertNotEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Delete mist from cart")
    void deleteCartItem_byId1_Ok() throws Exception {
        mockMvc.perform(
                        delete("/cart/cart-items/{cartItemId}", 1L))
                .andExpect(status().isNoContent());
    }

    private ShoppingCartResponseDto getCartResponseDto() {
        CartItemResponseDto cartItemResponseDto1 = new CartItemResponseDto();
        cartItemResponseDto1.setId(1L);
        cartItemResponseDto1.setBookId(1L);
        cartItemResponseDto1.setBookTitle("Book 1");
        cartItemResponseDto1.setQuantity(10);
        CartItemResponseDto cartItemResponseDto2 = new CartItemResponseDto();
        cartItemResponseDto2.setId(2L);
        cartItemResponseDto2.setBookId(2L);
        cartItemResponseDto2.setBookTitle("Book 2");
        cartItemResponseDto2.setQuantity(20);
        Set<CartItemResponseDto> cartItems = new HashSet<>();
        cartItems.add(cartItemResponseDto1);
        cartItems.add(cartItemResponseDto2);
        ShoppingCartResponseDto shoppingCartResponseDto = new ShoppingCartResponseDto();
        shoppingCartResponseDto.setUserId(1L);
        shoppingCartResponseDto.setCartItems(cartItems);
        return shoppingCartResponseDto;
    }
}
