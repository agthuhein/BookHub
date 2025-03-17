package com.bookhub.Repository.Mongo;

import com.bookhub.Model.Reviews;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReviewsRepository extends MongoRepository<Reviews, String> {
}
