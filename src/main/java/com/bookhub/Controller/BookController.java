package com.bookhub.Controller;

import com.bookhub.Model.Books;
import com.bookhub.Service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

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
//        try{
//            List<Books> books = bookService.getBooksByAuthor(authorId);
//            //Optional books = bookService.getBooksByAuthor(authorId);
//            if (!books.isEmpty()) {
//                return new ResponseEntity<>(books, HttpStatus.OK);
//            }
//            else {
//                return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
//            }
//        }
//        catch (Exception e) {
//            return new ResponseEntity<>("An error occurred while fetching the book by author.",
//                    HttpStatus.INTERNAL_SERVER_ERROR);
//        }
        List<Books> books = bookService.getBooksByAuthor(authorId);
        return ResponseEntity.ok(books);
    }
}
