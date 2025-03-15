package com.bookhub.Controller;

import com.bookhub.Model.Authors;
import com.bookhub.Service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AuthorController {

    private final AuthorService authorService;
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/getAllAuthors")
    public ResponseEntity<Object> getAllAuthors() {
        try{
            List<Authors> authors = authorService.getAllAuthors();
            return new ResponseEntity<>(authors, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>("An error occurred while fetching all the authors.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/api/admin/addAuthor")
    public ResponseEntity<Object> addAuthor(@Valid @RequestBody Authors author, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid author data." + result.getAllErrors());
        }
        try {
            authorService.addAuthor(author);
            return ResponseEntity.status(HttpStatus.CREATED).body("Successfully added author.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding author. " + e.getMessage());
        }
    }

    @PutMapping("/api/admin/updateAuthor/{authorId}")
    public ResponseEntity<Object> updateAuthor(@PathVariable("authorId") Integer authorId,
                                               @Valid @RequestBody Authors author,
                                               BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid author data." + result.getAllErrors());
        }
        try{
            authorService.updateAuthor(authorId, author);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully updated author.");
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating author. " + e.getMessage());
        }
    }

    @DeleteMapping("/api/admin/deleteAuthor/{authorId}")
    public ResponseEntity<Object> deleteAuthor(@PathVariable("authorId") Integer authorId) {
        try {
            authorService.deleteAuthor(authorId);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted author.");
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting author. " + e.getMessage());
        }
    }
}
