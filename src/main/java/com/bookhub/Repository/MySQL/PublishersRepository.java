package com.bookhub.Repository.MySQL;

import com.bookhub.Model.Publishers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublishersRepository extends JpaRepository<Publishers, Integer> {
    boolean existsByPublisherName(String publisherName);
}
