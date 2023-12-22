package mate.academy.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.List;
import mate.academy.model.Book;
import mate.academy.model.Category;
import mate.academy.repository.book.BookRepository;
import mate.academy.repository.category.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Find books by valid category id")
    void findBookByCategoriesId_ValidCategory_ExpectedTwoBooks() {
        Category category = new Category();
        category.setName("Action");
        category.setDescription("Action description");
        categoryRepository.save(category);
        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("Book 1");
        book1.setAuthor("Author 1");
        book1.setIsbn("1111");
        book1.setPrice(BigDecimal.valueOf(50));
        book1.setDescription("Description 1");
        book1.setCoverImage("Image 1");
        book1.getCategories().add(category);
        bookRepository.save(book1);
        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Book 2");
        book2.setAuthor("Author 2");
        book2.setIsbn("2222");
        book2.setPrice(BigDecimal.valueOf(100));
        book2.setDescription("Description 2");
        book2.setCoverImage("Image 2");
        book2.getCategories().add(category);
        bookRepository.save(book2);
        List<Book> actual = categoryRepository.getBooksByCategoriesId(category.getId());
        assertNotNull(actual);
        assertEquals(2, actual.size());
    }

    @Test
    @DisplayName("Get empty list of books by non-existent category")
    void findBooksByCategoriesId_WhereIdIs100_NotOk() {
        Long categoryId = 100L;
        List<Book> actual = categoryRepository.getBooksByCategoriesId(categoryId);
        assertNotNull(actual);
        assertEquals(0, actual.size());
    }
}
