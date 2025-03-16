package com.bookhub.Service;

import com.bookhub.CustomException.ResourceNotFoundException;
import com.bookhub.Model.Authors;
import com.bookhub.Model.Books;
import com.bookhub.Repository.AuthorsRepository;
import com.bookhub.Repository.BooksRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BooksRepository booksRepository;
    private final AuthorsRepository authorsRepository;
    public BookService(BooksRepository booksRepository, AuthorsRepository authorsRepository) {
        this.booksRepository = booksRepository;
        this.authorsRepository = authorsRepository;
    }

//    public List<BookDTO> getBookDetails() {
//        //return Collections.unmodifiableList(booksRepository.findAll());
//        return booksRepository.getAllBooks();
//    }
    //Get all Books
    @Transactional(readOnly = true)
    public List<Books> getAllBooks() {
        try {
            return booksRepository.findAll();
        }
        catch (DataAccessException e){
            throw new RuntimeException("Database access error occurred while fetching all books. " + e.getMessage());
        }
        catch (Exception e){
            throw new RuntimeException("An unexpected error occurred while fetching all categories.", e);
        }
    }

    @Transactional
    public Optional<Books> getBookById(Integer bookId) {
        if(bookId==null){
            throw new IllegalArgumentException("Book id cannot be null");
        }
        try{
            return booksRepository.findById(bookId);
        }
        catch (DataAccessException e){
            throw new RuntimeException("Database access error occurred while fetching book by ID. " + e.getMessage());
        }
        catch (Exception e){
            throw new RuntimeException("An unexpected error occurred while fetching book by ID. " + e.getMessage());
        }
    }

    @Transactional
    public Optional<Books> getBooksByTitle(String title) {
        if(title.isEmpty()){
            throw new IllegalArgumentException("Title cannot be empty.");
        }
        try {
            return booksRepository.findByTitleContainingIgnoreCase(title);
        }
        catch (DataAccessException e){
            throw new RuntimeException("Database access error occurred while fetching book by title. " + e.getMessage());
        }
        catch (Exception e){
            throw new RuntimeException("An unexpected error occurred while fetching book by title. " + e.getMessage());
        }
    }

    @Transactional
    public Optional<Books> getBookByISBN(String isbn) {
        if(isbn.isEmpty()){
            throw new IllegalArgumentException("ISBN cannot be empty.");
        }
        try{
            return booksRepository.findByIsbn(isbn);
        }
        catch (DataAccessException e){
            throw new RuntimeException("Database access error occurred while fetching book by ISBN. " + e.getMessage());
        }
        catch (Exception e){
            throw new RuntimeException("An unexpected error occurred while fetching book by ISBN. " + e.getMessage());
        }
    }

    @Transactional
    public List<Books> getBooksByAuthor(Integer authorId) {
        Optional<Authors> authors = authorsRepository.findById(authorId);
        if(authors.isEmpty()){
            throw new ResourceNotFoundException("Author not found");
        }
        try {
            return booksRepository.findByAuthorsContains(authors.get());
        }
        catch (DataAccessException e){
            throw new RuntimeException("Database access error occurred while fetching book by author. " + e.getMessage());
        }
        catch (Exception e){
            throw new RuntimeException("An unexpected error occurred while fetching book by author. " + e.getMessage());
        }

//        Authors author = authorsRepository.findById(authorId)
//                .orElseThrow(() -> new ResourceNotFoundException("Author with ID " + authorId + " not found"));
//        return booksRepository.findByAuthorsContains(author);
    }
}



