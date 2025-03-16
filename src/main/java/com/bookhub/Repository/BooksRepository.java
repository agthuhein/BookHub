package com.bookhub.Repository;

import com.bookhub.Model.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BooksRepository extends JpaRepository<Books, Integer> {
    Optional<Books> findByTitleContainingIgnoreCase(String title);
    //Optional<Books> findByIsdn(String isbn);

    Optional<Books> findByIsbn(String isbn);
}
