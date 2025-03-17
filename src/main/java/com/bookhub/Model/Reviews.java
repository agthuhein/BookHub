package com.bookhub.Model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Document(collection = "reviews")
public class Reviews {
    @Id
    private String id;
    private Integer bookId;
    private Integer userId;
    private String comment;
    private Integer rating;
    @CreatedDate
    private LocalDateTime reviewPosted;


    public Reviews() {
    }

    public Reviews(String id, Integer bookId, Integer userId, String comment, Integer rating, LocalDateTime reviewPosted) {
        this.id = id;
        this.bookId = bookId;
        this.userId = userId;
        this.comment = comment;
        this.rating = rating;
        this.reviewPosted = reviewPosted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public LocalDateTime getReviewPosted() {
        return reviewPosted;
    }

    public void setReviewPosted(LocalDateTime reviewPosted) {
        this.reviewPosted = reviewPosted;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Reviews reviews = (Reviews) o;
        return Objects.equals(id, reviews.id) && Objects.equals(bookId, reviews.bookId) && Objects.equals(userId, reviews.userId) && Objects.equals(comment, reviews.comment) && Objects.equals(rating, reviews.rating) && Objects.equals(reviewPosted, reviews.reviewPosted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bookId, userId, comment, rating, reviewPosted);
    }

    @Override
    public String toString() {
        return "Reviews{" +
                "id='" + id + '\'' +
                ", bookId=" + bookId +
                ", userId=" + userId +
                ", comment='" + comment + '\'' +
                ", rating=" + rating +
                ", reviewPosted=" + reviewPosted +
                '}';
    }
}
