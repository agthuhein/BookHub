package com.bookhub.Service;

import com.bookhub.CustomException.ResourceNotFoundException;
import com.bookhub.CustomException.UnauthorizedActionException;
import com.bookhub.Model.Books;
import com.bookhub.Model.Reviews;
import com.bookhub.Model.Users;
import com.bookhub.Repository.Mongo.ReviewsRepository;
import com.bookhub.Repository.MySQL.BooksRepository;
import com.bookhub.Repository.MySQL.UsersRepository;
import com.bookhub.Security.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewsRepository reviewsRepository;
    private final BooksRepository booksRepository;
    private final UsersRepository userRepository;
    private final JwtUtil jwtUtil;

    public ReviewService(ReviewsRepository reviewsRepository, BooksRepository booksRepository
            , JwtUtil jwtUtil, UsersRepository userRepository) {
        this.reviewsRepository = reviewsRepository;
        this.booksRepository = booksRepository;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
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
    public List<Reviews> getReviewsByBookId(Integer bookId){
        try{
            return reviewsRepository.getReviewsByBookId(bookId);
        }
        catch (DataAccessException e){
            throw new RuntimeException("Database error occurred while getting all reviews for given book id. " + e.getMessage());
        }
        catch (Exception e){
            throw new RuntimeException("An unexpected error occurred while getting all reviews for given book id. " + e.getMessage());
        }

    }

    @Transactional
    public void addReview(Reviews reviews, String token){
        Integer bookId = reviews.getBookId();
        Integer rating = reviews.getRating();
        String comment = reviews.getComment();
        Optional<Books> book = booksRepository.findById(bookId);

        Integer userId = jwtUtil.extractUserId(token);
        //System.out.println("ID in Review Service Class " + testUserId);

        if(book.isEmpty()){
            throw new ResourceNotFoundException ("There is no book with id " + bookId);
        }
//        if(comment == null || comment.isEmpty()){
//            throw new IllegalArgumentException("Comment cannot be empty");
//        }
        if(rating > 5 || rating < 0){
            throw new ResourceNotFoundException ("Invalid rating! Rating must be between 0 and 5");
        }
        // Check if the user has already reviewed the book
        Optional<Reviews> existingReview = reviewsRepository.findByUserIdAndBookId(userId, bookId);
        if (existingReview.isPresent()) {
            throw new IllegalArgumentException("You have already reviewed this book.");
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

    @Transactional
    public void updateReview(String reviewId, Reviews newReviews, String token){
        Optional<Reviews> existingReview = reviewsRepository.findById(reviewId);

        Integer loggedInUserId = jwtUtil.extractUserId(token);
        Integer rating = newReviews.getRating();
        String comment = newReviews.getComment();

       if(existingReview.isPresent()){
           Reviews ex_reviewInfo = existingReview.get();
           if(!ex_reviewInfo.getUserId().equals(loggedInUserId)){
               throw new IllegalArgumentException("You can only update your own reviews.");
           }
           if(comment == null || comment.isEmpty()){
               throw new IllegalArgumentException("Comment cannot be empty");
           }
           if(rating > 5 || rating < 0){
               throw new IllegalArgumentException("Invalid rating! Rating must be between 0 and 5");
           }
           ex_reviewInfo.setComment(newReviews.getComment());
           ex_reviewInfo.setRating(newReviews.getRating());
           try{
               reviewsRepository.save(ex_reviewInfo);
           }
           catch (DataAccessException e){
               throw new RuntimeException("Database error occurred while updating reviews. " + e.getMessage());
           }
           catch (Exception e){
               throw new RuntimeException("An unexpected error occurred while updating reviews. " + e.getMessage());
           }
       }
       else{
           throw new ResourceNotFoundException ("Review not found");
       }
    }

    @Transactional
    public void deleteReview(String reviewId) {
        String loggedInEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Users> loggedInUser = userRepository.findByEmail(loggedInEmail);

        if (loggedInUser.isEmpty()) {
            throw new UnauthorizedActionException("Unauthorized action. Please log in again.");
        }
        Integer loggedInUserId = loggedInUser.get().getUserId();
        String loginUserRole = loggedInUser.get().getRole();

        Optional<Reviews> toBeDeletedReview = reviewsRepository.findById(reviewId);
        if (toBeDeletedReview.isEmpty()) {
            throw new ResourceNotFoundException("Review not found.");
        }
        if (loginUserRole.equals("ADMIN")) {
            reviewsRepository.deleteById(reviewId);
        } else if (loginUserRole.equals("USER")) {
            if (!toBeDeletedReview.get().getUserId().equals(loggedInUserId)) {
                throw new UnauthorizedActionException("You can only delete your own reviews.");
            }
            reviewsRepository.deleteById(reviewId);
        }

    }
}
