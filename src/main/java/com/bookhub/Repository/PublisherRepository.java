package com.bookhub.Repository;

import com.bookhub.Model.Publishers;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PublisherRepository extends JpaRepository<Publishers, Integer> {
}
