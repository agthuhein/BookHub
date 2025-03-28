package com.bookhub.Controller;

import com.bookhub.CustomException.ResourceNotFoundException;
import com.bookhub.CustomException.UnauthorizedActionException;
import com.bookhub.Model.Reviews;
import com.bookhub.Service.ReviewService;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ReviewController {

    private final ReviewService reviewService;
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/getAllReviews")
    public ResponseEntity<Object> getAllReviews() {
        try{
            List<Reviews> reviews =  reviewService.getAllReviews();
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>("An error occurred while fetching all the reviews",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getReviewByBook/{bookId}")
    public ResponseEntity<Object> getReviewByBookId(@PathVariable("bookId") Integer bookId) {
        try{
            //Optional<Reviews> reviews = reviewService.getReviewsByBookId(bookId).stream().filter(b -> b.getBookId()
                    //.equals(bookId)).findFirst();
            List<Reviews> reviews =  reviewService.getReviewsByBookId(bookId);
            if (!reviews.isEmpty()) {
                return new ResponseEntity<>(reviews, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("No review for the book id: " + bookId, HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e){
            return new ResponseEntity<>("An error occurred while fetching all review for given book id. ",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addReview")
    public ResponseEntity<Object> addReview(@RequestBody Reviews review,
                                            @RequestHeader("Authorization") String token,
                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", bindingResult.getFieldErrors()
                    .stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList())));
        }
        try{
            reviewService.addReview(review, token.replace("Bearer ", ""));
            return ResponseEntity.status(HttpStatus.CREATED).body("Review added successfully");
        }
        catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding book. " +e.getMessage());
        }
    }

    @PutMapping("/updateReview/{reviewId}")
    public ResponseEntity<Object> updateReview(@PathVariable("reviewId") String reviewId,
                                               @RequestHeader("Authorization") String token,
                                               @RequestBody Reviews updatedReview,
                                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("errors", bindingResult.getFieldErrors()
                    .stream().map(error -> error.getDefaultMessage()).collect(Collectors.toList())));
        }
        try{
            reviewService.updateReview(reviewId, updatedReview, token.replace("Bearer ", ""));
            return ResponseEntity.status(HttpStatus.CREATED).body("Review updated successfully")  ;
        }
        catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating review. " +e.getMessage());
        }

    }

    @DeleteMapping("/deleteReview/{reviewId}")
    public ResponseEntity<Object> deleteReview(@PathVariable("reviewId") String reviewId) {
        try {
            reviewService.deleteReview(reviewId);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted review.");
        }
        catch (UnauthorizedActionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting review. " + e.getMessage());
        }
    }
}
