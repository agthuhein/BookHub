package com.bookhub.Controller;

import com.bookhub.CustomException.ResourceNotFoundException;
import com.bookhub.Model.Books;
import com.bookhub.Service.BookService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/getAllBooks")
    public ResponseEntity<Object> getBookDetails() {
        try {
            List<Books> books = bookService.getAllBooks();
            return new ResponseEntity<>(books, HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>("An error occurred while fetching all the books.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getBookById/{bookId}")
    public ResponseEntity<Object> getBookById(@PathVariable("bookId") Integer bookId) {

        try{
            Optional book = bookService.getBookById(bookId).stream().filter(b -> b.getBookId()
            .equals(bookId)).findFirst();
            if (book.isPresent()) {
                return new ResponseEntity<>(book.get(), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e) {
            return new ResponseEntity<>("An error occurred while fetching the book by id.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/getBookByTitle")
    public ResponseEntity<Object> getBookByTitle(@RequestParam("title") String title) {
        try{
            Optional<Books> books = bookService.getBooksByTitle(title);
            if (books.isPresent()) {
                return new ResponseEntity<>(books.get(), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e) {
            return new ResponseEntity<>("An error occurred while fetching book by title.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getBookByISBN/{isbn}")
    public ResponseEntity<Object> getBookByISBN(@PathVariable("isbn") String isbn) {
        try{
            Optional<Books> book = bookService.getBookByISBN(isbn);
            if (book.isPresent()) {
                return new ResponseEntity<>(book, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e) {
            return new ResponseEntity<>("An error occurred while fetching the book by ISBN.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/getBookByAuthor/{authorId}")
    public ResponseEntity<Object> getBookByAuthor(@PathVariable("authorId") Integer authorId) {
        try{
            List<Books> books = bookService.getBooksByAuthor(authorId);
            if (!books.isEmpty()) {
                return new ResponseEntity<>(books, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("Book not found for the author.", HttpStatus.NOT_FOUND);
            }
        }
        catch (ResourceNotFoundException e)
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<>("An error occurred while fetching the book by author.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getBookByCategory/{categoryId}")
    public ResponseEntity<Object> getBookByCategory(@PathVariable("categoryId") Integer categoryId) {
        try{
            List<Books> books = bookService.getBooksByCategory(categoryId);
            if (!books.isEmpty()) {
                return new ResponseEntity<>(books, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("Book not found for category.", HttpStatus.NOT_FOUND);
            }
        }
        catch (ResourceNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<>("An error occurred while fetching the book by Category.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/getBookByPublisher/{publisherId}")
    public ResponseEntity<Object> getBooksByPublisher(@PathVariable("publisherId") Integer publisherId) {
        try {
            List<Books> books = bookService.getBooksByPublisher(publisherId);
            if (!books.isEmpty()) {
                return new ResponseEntity<>(books, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("Book not found for the publisher.", HttpStatus.NOT_FOUND);
            }
        }
        catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (Exception e) {
            return new ResponseEntity<>("An error occurred while fetching the book by publisher.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //Add new book
    @PostMapping("/api/admin/addNewBook")
    public ResponseEntity<Object> addNewBook(@Valid @RequestBody Books book, BindingResult result) {
        if (result.hasErrors()) {
            //return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid book data. " + result.getAllErrors());
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("errors", errors));
        }
        try{
            bookService.addNewBook(book);
            return ResponseEntity.status(HttpStatus.CREATED).body("Successfully added a book.");
        }
        catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding book. " +e.getMessage());
        }
    }
}
