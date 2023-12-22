package mate.academy.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mate.academy.dto.book.BookDto;
import mate.academy.dto.book.CreateBookRequestDto;
import mate.academy.mapper.BookMapper;
import mate.academy.model.Book;
import mate.academy.repository.book.BookRepository;
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

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    private static final long NON_EXISTING_BOOK_ID = 100L;
    private static final double UPDATED_PRICE = 1.11;
    private static final String NON_EXISTING_ID_EXCEPTION = "Can't find book by id ";
    @InjectMocks
    private BookServiceImpl bookService;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;

    @Test
    @DisplayName("Verifying method getAll")
    void getAll_ValidPageable_ReturnAllBooks() {
        List<Book> books = new ArrayList<>();
        books.add(getABookWithPrice19_99());
        books.add(getBBookWithPrice19_99());

        List<BookDto> bookDtosExpected = new ArrayList<>();
        bookDtosExpected.add(getABookDtoWithPrice19_99());
        bookDtosExpected.add(getBBookDtoWithPrice19_99());

        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        Mockito.when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        Mockito.when(bookMapper.toDto(books.get(0))).thenReturn(bookDtosExpected.get(0));
        Mockito.when(bookMapper.toDto(books.get(1))).thenReturn(bookDtosExpected.get(1));

        List<BookDto> bookDtosActual = bookService.findAll(pageable);

        assertEquals(2, bookDtosActual.size());
        assertEquals(bookDtosActual, bookDtosExpected);
        Mockito.verify(bookRepository, Mockito.times(1)).findAll(pageable);
        Mockito.verify(bookMapper, Mockito.times(1)).toDto(books.get(0));
        Mockito.verify(bookMapper, Mockito.times(1)).toDto(books.get(1));
        Mockito.verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Check if findById() method throws correct "
            + "exception with non existing book id")
    public void findBook_GivenNonExistingBookId_ShouldThrowException() {
        Long nonExistingBookId = NON_EXISTING_BOOK_ID;
        Mockito.when(bookRepository.findById(nonExistingBookId)).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> bookService.findById(nonExistingBookId)
        );
        String expected = "Can't find book by ID: " + nonExistingBookId;
        String actual = exception.getMessage();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Check if update() method throws correct "
            + "exception with non existing book id")
    public void update_GivenNonExistingBookId_ShouldThrowException() {
        CreateBookRequestDto updateBookRequestDto = getUpdateBookRequestDto();
        updateBookRequestDto.setPrice(BigDecimal.valueOf(UPDATED_PRICE));
        Long nonExistingBookId = NON_EXISTING_BOOK_ID;
        Mockito.when(bookRepository.findById(nonExistingBookId)).thenReturn(Optional.empty());
        Exception exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> bookService.update(nonExistingBookId, updateBookRequestDto)
        );
        String expected = NON_EXISTING_ID_EXCEPTION + nonExistingBookId;
        String actual = exception.getMessage();
        Assertions.assertEquals(expected, actual);
    }

    private CreateBookRequestDto getUpdateBookRequestDto() {
        return new CreateBookRequestDto()
                .setTitle("updated")
                .setAuthor("updated")
                .setIsbn("updated")
                .setPrice(BigDecimal.valueOf(UPDATED_PRICE));
    }

    private Book getABookWithPrice19_99() {
        return new Book()
                .setId(1L)
                .setTitle("A Book")
                .setAuthor("A Author")
                .setIsbn("A-ISBN")
                .setPrice(BigDecimal.valueOf(19.99))
                .setDescription("A description")
                .setCoverImage("A cover image");

    }

    private Book getBBookWithPrice19_99() {
        return new Book()
                .setId(2L)
                .setTitle("B Book")
                .setAuthor("B Author")
                .setIsbn("B-ISBN")
                .setPrice(BigDecimal.valueOf(19.99))
                .setDescription("B description")
                .setCoverImage("B cover image");

    }

    private Book getCBookWithPrice21_99() {
        return new Book()
                .setId(3L)
                .setTitle("C Book")
                .setAuthor("C Author")
                .setIsbn("C-ISBN")
                .setPrice(BigDecimal.valueOf(21.99))
                .setDescription("C description")
                .setCoverImage("C cover image");

    }

    private BookDto getABookDtoWithPrice19_99() {
        return new BookDto()
                .setId(1L)
                .setTitle("A Book")
                .setAuthor("A Author")
                .setIsbn("A-ISBN")
                .setPrice(BigDecimal.valueOf(19.99))
                .setDescription("A description")
                .setCoverImage("A cover image");

    }

    private BookDto getBBookDtoWithPrice19_99() {
        return new BookDto()
                .setId(2L)
                .setTitle("B Book")
                .setAuthor("B Author")
                .setIsbn("B-ISBN")
                .setPrice(BigDecimal.valueOf(19.99))
                .setDescription("B description")
                .setCoverImage("B cover image");

    }

    private BookDto getCBookDtoWithPrice21_99() {
        return new BookDto()
                .setId(3L)
                .setTitle("C Book")
                .setAuthor("C Author")
                .setIsbn("C-ISBN")
                .setPrice(BigDecimal.valueOf(21.99))
                .setDescription("C description")
                .setCoverImage("C cover image");

    }

    private BookDto getBookDto(Book book) {
        return new BookDto()
                .setTitle(book.getTitle())
                .setAuthor(book.getAuthor())
                .setIsbn(book.getIsbn())
                .setPrice(book.getPrice())
                .setDescription(book.getDescription())
                .setCoverImage(book.getCoverImage());
    }

    private Book getBook(CreateBookRequestDto requestDto) {
        return new Book()
                .setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setIsbn(requestDto.getIsbn())
                .setPrice(requestDto.getPrice())
                .setDescription(requestDto.getDescription())
                .setCoverImage(requestDto.getCoverImage());
    }

    private CreateBookRequestDto getCreateTestBookRequestDto() {
        return new CreateBookRequestDto()
                .setTitle("Test-Book")
                .setAuthor("Test-Author")
                .setIsbn("Test-ISBN")
                .setPrice(BigDecimal.valueOf(44.95))
                .setDescription("Test-Description")
                .setCoverImage("Test-Cover-Image");
    }
}
