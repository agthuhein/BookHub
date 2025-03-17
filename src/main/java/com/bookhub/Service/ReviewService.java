package com.bookhub.Service;

import com.bookhub.CustomException.ResourceNotFoundException;
import com.bookhub.Model.Books;
import com.bookhub.Model.Reviews;
import com.bookhub.Repository.Mongo.ReviewsRepository;
import com.bookhub.Repository.MySQL.BooksRepository;
import com.bookhub.Security.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewsRepository reviewsRepository;
    private final BooksRepository booksRepository;
    private final JwtUtil jwtUtil;

    public ReviewService(ReviewsRepository reviewsRepository, BooksRepository booksRepository
            , JwtUtil jwtUtil) {
        this.reviewsRepository = reviewsRepository;
        this.booksRepository = booksRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public List<Reviews> getAllReviews(){
        try{
            return reviewsRepository.findAll();
        }
        catch (DataAccessException e){
            throw new RuntimeException("Database error occurred while getting all reviews. " + e.getMessage());
        }
        catch (Exception e){
            throw new RuntimeException("An unexpected error occurred while getting all reviews. " + e.getMessage());
        }
    }

    @Transactional
    public List<Reviews> getReviewsByBookId(Long bookId){
        return null;
    }

    @Transactional
    public void addReview(Integer bookId, Integer rating, Reviews reviews, String token){
        //Optional<Users> user = userRepository.findById(userId);
        Optional<Books> book = booksRepository.findById(bookId);

        Integer userId = jwtUtil.extractUserId(token);
        //System.out.println("ID in Review Service Class " + testUserId);

        if(book.isEmpty()){
            throw new ResourceNotFoundException ("Book not found");
        }
        if(rating > 5 || rating < 0){
            throw new ResourceNotFoundException ("Invalid rating! Rating must be between 0 and 5");
        }
        try{
            reviews.setUserId(userId);
            reviewsRepository.save(reviews);
        }
        catch (DataAccessException e){
            throw new RuntimeException("Database error occurred while saving reviews. " + e.getMessage());
        }
        catch (Exception e){
            throw new RuntimeException("An unexpected error occurred while saving reviews. " + e.getMessage());
        }
    }



    //
//    public boolean isValidBookId(String bookId) {
//        Integer count = jdbcTemplate.queryForObject(
//                "SELECT COUNT(*) FROM books WHERE id = ?", Integer.class, bookId);
//        return count != null && count > 0;
//    }
//
//    public boolean isValidUserId(String userId) {
//        Integer count = jdbcTemplate.queryForObject(
//                "SELECT COUNT(*) FROM users WHERE id = ?", Integer.class, userId);
//        return count != null && count > 0;
//    }
//
//    public Reviews saveReview(Reviews review) {
//        if (!isValidBookId(review.getBookId()) || !isValidUserId(review.getUserId())) {
//            throw new IllegalArgumentException("Invalid bookId or userId");
//        }
//        return reviewsRepository.save(review);
//    }

}
