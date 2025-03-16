package com.bookhub.Repository;

import com.bookhub.Model.Publishers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublishersRepository extends JpaRepository<Publishers, Integer> {
    boolean existsByPublisherName(String publisherName);
}
