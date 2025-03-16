package com.bookhub.Repository;

import com.bookhub.Model.Authors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorsRepository extends JpaRepository<Authors, Integer> {
    boolean existsByAuthorName(String authorName);
}
