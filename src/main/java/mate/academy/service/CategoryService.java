package mate.academy.service;

import java.util.List;
import mate.academy.dto.category.BookDtoWithoutCategoryIds;
import mate.academy.dto.category.CategoryRequestDto;
import mate.academy.dto.category.CategoryResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

public interface CategoryService {
    List<CategoryResponseDto> findAll(Pageable pageable);

    CategoryResponseDto getById(Long id);

    CategoryResponseDto save(CategoryRequestDto requestDto);

    CategoryResponseDto update(Long id, CategoryRequestDto requestDto);

    void deleteById(Long id);

    @Query("FROM Book b JOIN b.categories c WHERE c.id = :categoryId")
    List<BookDtoWithoutCategoryIds> getBooksByCategoriesId(Long id);
}
