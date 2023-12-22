package mate.academy.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import mate.academy.dto.category.CategoryRequestDto;
import mate.academy.dto.category.CategoryResponseDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.CategoryMapper;
import mate.academy.model.Category;
import mate.academy.repository.category.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @DisplayName("Find all categories")
    @Test
    void findAll_ValidCategoryDto_Success() {
        // given
        Category category = getCategoryA();
        CategoryResponseDto categoryResponseDto = getCreateCategoryResponseDto();
        Pageable pageable = PageRequest.of(0, 10);
        List<Category> categories = List.of(category);
        Page<Category> categoryPage = new PageImpl<>(
                categories, pageable, categories.size());
        Mockito.when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        Mockito.when(categoryMapper.toDto(category)).thenReturn(categoryResponseDto);
        // when
        List<CategoryResponseDto> actual = categoryService.findAll(pageable);
        // then
        Assertions.assertEquals(1, actual.size());
        CategoryResponseDto dto = actual.get(0);
        Assertions.assertNotNull(dto);
        EqualsBuilder.reflectionEquals(categoryResponseDto, dto);
    }

    @Test
    @DisplayName("Find all empty category list")
    void findAll_EmptyCategoryList_False() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> emptyCategoryPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        when(categoryRepository.findAll(pageable)).thenReturn(emptyCategoryPage);
        // when
        List<CategoryResponseDto> actual = categoryService.findAll(pageable);
        // then
        assertTrue(actual.isEmpty());
    }

    @Test
    @DisplayName("Find an existing category by valid ID")
    void getById_WithValidCategoryId_ShouldReturnValidCategory() {
        // given
        Category category = getCategoryA();
        Long categoryId = category.getId();
        CategoryResponseDto expected = getCreateCategoryResponseDto();

        Mockito.when(categoryRepository.findById(anyLong()))
                .thenReturn(Optional.of(category));
        Mockito.when(categoryMapper.toDto(category))
                .thenReturn(expected);
        // when
        CategoryResponseDto actual = categoryService.getById(categoryId);
        // Then
        assertEquals(expected, actual);
    }

    @DisplayName("Get category by non existing ID")
    @Test
    void getById_WithNonExistingId_ShouldThrowException() {
        // given
        Long testId = 100L;
        Mockito.when(categoryRepository.findById(testId))
                .thenReturn(Optional.empty());
        //when
        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> categoryService.getById(testId));
        // then
        String actual = exception.getMessage();
        String expected = "Can't find category by id = " + testId;
        Assertions.assertEquals(expected, actual);
    }

    @DisplayName("Save new category")
    @Test
    void saveCategory_ValidCategoryDto_Success() {
        // given
        Category category = getCategoryA();
        CategoryResponseDto expected = getCreateCategoryResponseDto();
        CategoryRequestDto createCategoryRequestDto = getCreateCategoryRequestDto();
        Mockito.when(categoryRepository.save(category)).thenReturn(category);
        Mockito.when(categoryMapper.toEntity(createCategoryRequestDto))
                .thenReturn(category);
        // when
        CategoryResponseDto actual = categoryService.save(createCategoryRequestDto);
        // then
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @DisplayName("Update category by ID")
    @Test
    void update_ValidCategoryDto_Success() {
        // given
        Category category = getCategoryA();
        CategoryResponseDto expected = getCreateCategoryResponseDto();
        CategoryRequestDto createCategoryRequestDto = getCreateCategoryRequestDto();
        Long categoryId = category.getId();
        Mockito.when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        Mockito.when(categoryRepository.save(category)).thenReturn(category);
        Mockito.when(categoryMapper.toDto(category)).thenReturn(expected);
        // when
        CategoryResponseDto actual = categoryService.update(categoryId, createCategoryRequestDto);
        // then
        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @DisplayName("Update category by non existing ID")
    @Test
    void update_WithNonExistingId_ShouldThrowException() {
        // given
        CategoryRequestDto createCategoryRequestDto = getCreateCategoryRequestDto();
        Long testId = 100L;
        Mockito.when(categoryRepository.findById(testId))
                .thenReturn(Optional.empty());
        // when
        EntityNotFoundException exception =
                assertThrows(EntityNotFoundException.class,
                        () -> categoryService.update(testId, createCategoryRequestDto));
        // then
        String actual = exception.getMessage();
        String expected = "Can't find category by id " + testId;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Delete category by ID")
    void deleteCategory_validCategoryId_True() {
        Category category = getCategoryA();
        assertDoesNotThrow(() -> categoryService.deleteById(category.getId()));
    }

    private Category getCategoryA() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Category A");
        category.setDescription("Description category A");
        return category;
    }

    private CategoryRequestDto getCreateCategoryRequestDto() {
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto();
        categoryRequestDto.setName("Category A");
        categoryRequestDto.setDescription("Description category A");
        return categoryRequestDto;
    }

    private CategoryResponseDto getCreateCategoryResponseDto() {
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setId(1L);
        categoryResponseDto.setName("Category A");
        categoryResponseDto.setDescription("Description category A");
        return categoryResponseDto;
    }
}
