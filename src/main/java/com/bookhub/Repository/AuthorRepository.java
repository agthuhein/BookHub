package com.bookhub.Repository;

import com.bookhub.Model.Authors;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Authors, Integer> {
    boolean existsByAuthorName(String authorName);
}
