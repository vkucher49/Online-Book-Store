package mate.academy.service;

import java.util.List;
import mate.academy.dto.BookDto;
import mate.academy.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto findById(Long id);

    void deleteById(Long id);

    BookDto update(Long id, CreateBookRequestDto updateBookDto);
}
