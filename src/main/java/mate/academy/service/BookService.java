package mate.academy.service;

import java.util.List;
import mate.academy.dto.book.BookDto;
import mate.academy.dto.book.BookSearchParameters;
import mate.academy.dto.book.CreateBookRequestDto;
import mate.academy.dto.category.BookDtoWithoutCategoryIds;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    BookDto update(Long id, CreateBookRequestDto createBookRequestDto);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParameters searchParameters);

    List<BookDtoWithoutCategoryIds> findAllByCategoryId(Long categoryId);
}
