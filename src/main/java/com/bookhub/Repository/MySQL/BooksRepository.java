package com.bookhub.Repository.MySQL;

import com.bookhub.Model.Authors;
import com.bookhub.Model.Books;
import com.bookhub.Model.Categories;
import com.bookhub.Model.Publishers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BooksRepository extends JpaRepository<Books, Integer> {
    Optional<Books> findByTitleContainingIgnoreCase(String title);
    Optional<Books> findByIsbn(String isbn);
    List<Books> findByAuthorsContains(Authors authors);
    List<Books> findByCategories(Categories categories);
    List<Books> findByPublishers(Publishers publishers);
    boolean existsByPublishers(Publishers publishers);
    boolean existsByCategories(Categories categories);
    //boolean existsByAuthors(Set<Authors> authors);
    @Query("SELECT COUNT(b) > 0 FROM Books b INNER JOIN b.authors a WHERE a.authorId = :authorId")
    boolean existsByAuthors(@Param("authorId") Integer authorId);
}
