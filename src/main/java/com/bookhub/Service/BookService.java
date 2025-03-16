package com.bookhub.Service;

import com.bookhub.Model.Books;
import com.bookhub.Repository.BooksRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BooksRepository booksRepository;
    public BookService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

//    public List<BookDTO> getBookDetails() {
//        //return Collections.unmodifiableList(booksRepository.findAll());
//        return booksRepository.getAllBooks();
//    }
    public List<Books> getAllBooks() { return booksRepository.findAll(); }
    public Books getBookById(Long id) { return booksRepository.findById(id).orElse(null); }
    public Books saveBook(Books book) { return booksRepository.save(book); }
    public void deleteBook(Long id) { booksRepository.deleteById(id); }

}
