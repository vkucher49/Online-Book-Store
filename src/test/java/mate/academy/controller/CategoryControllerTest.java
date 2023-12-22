package mate.academy.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import mate.academy.dto.category.CategoryRequestDto;
import mate.academy.dto.category.CategoryResponseDto;
import mate.academy.service.CategoryServiceImpl;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CategoryServiceImpl categoryService;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "data/books/remove-all-data.sql"
                    )
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("data/books/add-two-categories.sql")
            );
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
                    "data/books/remove-all-categories.sql"
            ));
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = "classpath:data/books/delete-action-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Create a new category with valid data")
    void createCategory_ValidRequestDto_Success() throws Exception {
        // given
        CategoryRequestDto categoryDto = new CategoryRequestDto();
        categoryDto.setName("Action");
        categoryDto.setDescription("Description category action");
        CategoryResponseDto expected = new CategoryResponseDto();
        expected.setName(categoryDto.getName());
        expected.setDescription(categoryDto.getDescription());
        // when
        when(categoryService.save(any(CategoryRequestDto.class))).thenReturn(expected);
        MvcResult mvcResult = mockMvc.perform(post("/categories")
                        .content(objectMapper.writeValueAsString(categoryDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        // then
        CategoryResponseDto actual = objectMapper.readValue(mvcResult.getResponse()
                .getContentAsString(), CategoryResponseDto.class);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    @DisplayName("Find all existent categories")
    void getAllCategories_ShouldReturnAllCategories_Success3() throws Exception {
        // given
        CategoryResponseDto categoryDtoA = new CategoryResponseDto();
        categoryDtoA.setName("Category 1");
        categoryDtoA.setDescription("Description category 1");
        CategoryResponseDto categoryDtoB = new CategoryResponseDto();
        categoryDtoB.setName("Category 2");
        categoryDtoB.setDescription("Description category 2");
        CategoryResponseDto categoryDtoC = new CategoryResponseDto();
        categoryDtoC.setName("Category 3");
        categoryDtoC.setDescription("Description category 3");
        List<CategoryResponseDto> expectedCategories =
                Arrays.asList(categoryDtoA, categoryDtoB, categoryDtoC);
        // when
        when(categoryService.findAll(any(Pageable.class))).thenReturn(expectedCategories);
        MvcResult result = mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andReturn();
        // then
        CategoryResponseDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), CategoryResponseDto[].class);
        Assertions.assertEquals(expectedCategories.size(), Arrays.stream(actual).toList().size());
        EqualsBuilder.reflectionEquals(expectedCategories, Arrays.stream(actual).toList(), "id");
    }

    @Test
    @WithMockUser
    @DisplayName("Get existent category by valid ID")
    void getCategoryById_ValidCategoryId_Success() throws Exception {
        // given
        Long categoryId = 1L;
        CategoryResponseDto categoryDto = new CategoryResponseDto();
        categoryDto.setId(categoryId);
        categoryDto.setName("Category A");
        categoryDto.setDescription("Description category A");
        // when
        when(categoryService.getById(categoryId)).thenReturn(categoryDto);
        MvcResult result = mockMvc.perform(
                        get("/categories/{id}", categoryId)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        // then
        CategoryResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryResponseDto.class);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(1L, actual.getId());
        Assertions.assertEquals("Category A", actual.getName());
        Assertions.assertEquals("Description category A", actual.getDescription());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Delete category by ID")
    void deleteCategory_ValidCategoryId_Success() throws Exception {
        CategoryResponseDto categoryDtoA = new CategoryResponseDto();
        categoryDtoA.setId(1L);
        categoryDtoA.setName("Category A");
        categoryDtoA.setDescription("Description category A");
        Long categoryId = 1L;
        mockMvc.perform(delete("/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @DisplayName("Update category by ID")
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateCategory_ValidUpdatedCategoryDto_Success() throws Exception {
        // given
        Long categoryId = 1L;
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto();
        categoryRequestDto.setName("Category");
        categoryRequestDto.setDescription("Description category");
        CategoryResponseDto expected = new CategoryResponseDto();
        expected.setId(categoryId);
        expected.setDescription(categoryRequestDto.getDescription());
        expected.setName(categoryRequestDto.getName());
        String jsonRequest = objectMapper.writeValueAsString(categoryRequestDto);
        // when
        Mockito.when(categoryService.update(Mockito.eq(categoryId),
                        Mockito.any(CategoryRequestDto.class)))
                .thenReturn(expected);
        MvcResult result = mockMvc.perform(put("/categories/{id}", categoryId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        // then
        CategoryResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryResponseDto.class);
        Assertions.assertNotNull(actual);
        assertEquals(expected, actual);
    }
}
