package com.bookhub.Repository.MySQL;

import com.bookhub.Model.Authors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorsRepository extends JpaRepository<Authors, Integer> {
    boolean existsByAuthorName(String authorName);
}
