package com.bookhub.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "books")
public class Books {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Integer bookId;

    @Column(name = "isbn", unique = true, nullable = false)
    @NotBlank(message = "ISBN cannot be empty.")
    @Size(min = 10, max = 13, message = "ISBN must be between 10 and 13 characters.")
    private String isbn;

    @Column(name = "title", nullable = false)
    @NotBlank(message = "Title cannot be empty.")
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "Description cannot be empty.")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    @DecimalMin(value = "0.01", message = "Price must be greater than 0.")
    @NotNull(message = "Price is required.")
    private BigDecimal price;

    @Column(name = "stock_quantity", nullable = false)
    @Min(value = 0, message = "Stock quantity cannot be negative.")
    @NotNull(message = "Stock quantity is required.")
    private Integer stockQuantity;

    @Column(name = "published_date")
    private LocalDate publishedDate;

    //Relationships
    @ManyToOne
    @JoinColumn(name = "publisher_id", referencedColumnName = "publisher_id")
    private Publishers publishers;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    private Categories categories;

    @ManyToMany
    @JoinTable(
            name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    //private List<Authors> authors;
    private Set<Authors> authors;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Books() {
    }

    public Books(Integer bookId, String isbn, String title, String description, BigDecimal price, Integer stockQuantity, LocalDate publishedDate, Publishers publishers, Categories categories, Set<Authors> authors, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.title = title;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.publishedDate = publishedDate;
        this.publishers = publishers;
        this.categories = categories;
        this.authors = authors;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    public Publishers getPublishers() {
        return publishers;
    }

    public void setPublishers(Publishers publishers) {
        this.publishers = publishers;
    }

    public Categories getCategories() {
        return categories;
    }

    public void setCategories(Categories categories) {
        this.categories = categories;
    }

    public Set<Authors> getAuthors() {
        return authors;
    }
    public void setAuthors(Set<Authors> authors) {
        this.authors = authors;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Books books = (Books) o;
        return Objects.equals(bookId, books.bookId) && Objects.equals(isbn, books.isbn) && Objects.equals(title, books.title) && Objects.equals(description, books.description) && Objects.equals(price, books.price) && Objects.equals(stockQuantity, books.stockQuantity) && Objects.equals(publishedDate, books.publishedDate) && Objects.equals(publishers, books.publishers) && Objects.equals(categories, books.categories) && Objects.equals(authors, books.authors) && Objects.equals(createdAt, books.createdAt) && Objects.equals(updatedAt, books.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, isbn, title, description, price, stockQuantity, publishedDate, publishers, categories, authors, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "Books{" +
                "bookId=" + bookId +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                ", publishedDate=" + publishedDate +
                ", publishers=" + publishers +
                ", categories=" + categories +
                ", authors=" + authors +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // PreUpdate to update timestamp
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
