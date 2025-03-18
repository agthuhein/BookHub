package com.bookhub.Repository.Mongo;

import com.bookhub.Model.Reviews;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewsRepository extends MongoRepository<Reviews, String> {
    List<Reviews> getReviewsByBookId(Integer bookId);
    Optional<Reviews> findByUserIdAndBookId(Integer userId, Integer bookId);
    Optional<Reviews> findById(String reviewId);
}
