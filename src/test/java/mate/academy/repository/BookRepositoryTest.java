package mate.academy.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import mate.academy.model.Book;
import mate.academy.repository.book.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
public class BookRepositoryTest {

    @Mock
    private BookRepository bookRepository;

    @Test
    public void testFindAllByCategoriesId() {
        Long categoryId = 1L;
        Book book = new Book();
        when(bookRepository.findAllByCategoriesId(categoryId))
                .thenReturn(Collections.singletonList(book));

        List<Book> books = bookRepository.findAllByCategoriesId(categoryId);

        assertEquals(1, books.size());
        assertEquals(book, books.get(0));
    }
}
