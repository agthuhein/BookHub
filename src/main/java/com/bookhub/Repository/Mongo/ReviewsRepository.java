package com.bookhub.Repository.Mongo;

import com.bookhub.Model.Reviews;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewsRepository extends MongoRepository<Reviews, String> {
    List<Reviews> getReviewsByBookId(Integer bookId);
}
