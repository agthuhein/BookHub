package com.bookhub.Repository;

import com.bookhub.Model.Authors;
import com.bookhub.Model.Books;
import com.bookhub.Model.Categories;
import com.bookhub.Model.Publishers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BooksRepository extends JpaRepository<Books, Integer> {
    Optional<Books> findByTitleContainingIgnoreCase(String title);
    Optional<Books> findByIsbn(String isbn);
    List<Books> findByAuthorsContains(Authors authors);
    List<Books> findByCategories(Categories categories);
    List<Books> findByPublishers(Publishers publishers);
}
