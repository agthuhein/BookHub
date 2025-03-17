package com.bookhub.Controller;

import com.bookhub.CustomException.ResourceNotFoundException;
import com.bookhub.Model.Reviews;
import com.bookhub.Service.ReviewService;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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

    @PostMapping("/addReview")
    public ResponseEntity<Object> addReview(@RequestBody Reviews review,
                                            @RequestHeader("Authorization") String token,
                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", bindingResult.getFieldErrors()
                    .stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList())));
        }
        Integer userId = review.getUserId();
        Integer bookId = review.getBookId();
        Integer rating = review.getRating();
        try{
            reviewService.addReview(bookId, rating, review, token.replace("Bearer ", ""));
            return ResponseEntity.status(HttpStatus.CREATED).body("Review added successfully")  ;
        }
        catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding book. " +e.getMessage());
        }
    }
}
