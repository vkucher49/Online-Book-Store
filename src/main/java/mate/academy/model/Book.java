package mate.academy.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import lombok.Data;

@Entity
@Data
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private String coverImage;
    private String description;

}
