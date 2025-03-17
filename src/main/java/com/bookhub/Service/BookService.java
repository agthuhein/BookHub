package com.bookhub.Service;

import com.bookhub.CustomException.ResourceNotFoundException;
import com.bookhub.Model.Authors;
import com.bookhub.Model.Books;
import com.bookhub.Model.Categories;
import com.bookhub.Model.Publishers;
import com.bookhub.Repository.MySQL.AuthorsRepository;
import com.bookhub.Repository.MySQL.BooksRepository;
import com.bookhub.Repository.MySQL.CategoriesRepository;
import com.bookhub.Repository.MySQL.PublishersRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BooksRepository booksRepository;
    private final AuthorsRepository authorsRepository;
    private final CategoriesRepository categoriesRepository;
    private final PublishersRepository publishersRepository;
    public BookService(BooksRepository booksRepository, AuthorsRepository authorsRepository,
                       CategoriesRepository categoriesRepository, PublishersRepository publishersRepository) {
        this.booksRepository = booksRepository;
        this.authorsRepository = authorsRepository;
        this.categoriesRepository = categoriesRepository;
        this.publishersRepository = publishersRepository;
    }

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

    }

    @Transactional
    public List<Books> getBooksByCategory(Integer categoryId) {
        Optional<Categories> categories = categoriesRepository.findById(categoryId);
        if(categories.isEmpty()){
            throw new ResourceNotFoundException("Category not found");
        }
        try{
            return booksRepository.findByCategories(categories.get());
        }
        catch (DataAccessException e){
            throw new RuntimeException("Database access error occurred while fetching book by Category. " + e.getMessage());
        }
        catch (Exception e){
            throw new RuntimeException("An unexpected error occurred while fetching book by Category. " + e.getMessage());
        }
    }

    @Transactional
    public List<Books> getBooksByPublisher(Integer publisherId) {
        Optional<Publishers> publishers = publishersRepository.findById(publisherId);
        if(publishers.isEmpty()){
            throw new ResourceNotFoundException("Publisher not found");
        }
        try{
            return booksRepository.findByPublishers(publishers.get());
        }
        catch (DataAccessException e){
            throw new RuntimeException("Database access error occurred while fetching book by Publisher. " + e.getMessage());
        }
        catch (Exception e){
            throw new RuntimeException("An unexpected error occurred while fetching book by Publisher. " + e.getMessage());
        }
    }

    @Transactional
    public void addNewBook(Books book) {
        Optional<Books> books = booksRepository.findByIsbn(book.getIsbn());
        //int stockQuantity = book.getStockQuantity();
        BigDecimal price = book.getPrice();
        if(books.isPresent()){
            throw new IllegalStateException("A book with this ISBN already exists.");
        }
        if(price.compareTo(BigDecimal.ZERO)<0){
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
//        if(stockQuantity <= 0){
//            throw new IllegalArgumentException("Stock quantity cannot be 0 or less than 0.");
//        }
        try{
            booksRepository.save(book);
        }
        catch (DataAccessException e){
            throw new RuntimeException("Database access error occurred while adding a new book. " + e.getMessage());
        }
        catch (Exception e){
            throw new RuntimeException("An unexpected error occurred while adding a new book. " + e.getMessage());
        }

    }

    @Transactional
    public void updateBook(Integer bookId, Books updateBook) {
        if(!booksRepository.existsById(bookId)){
            throw new ResourceNotFoundException("Book ID: " + bookId + " does not exist.");
        }
        Books existingBook = booksRepository.findById(bookId).get();

        existingBook.setIsbn(updateBook.getIsbn());
        existingBook.setTitle(updateBook.getTitle());
        existingBook.setDescription(updateBook.getDescription());
        existingBook.setPrice(updateBook.getPrice());
        existingBook.setStockQuantity(updateBook.getStockQuantity());
        existingBook.setPublishedDate(updateBook.getPublishedDate());
        existingBook.setPublishers(updateBook.getPublishers());
        existingBook.setCategories(updateBook.getCategories());
        existingBook.setAuthors(updateBook.getAuthors());

        try{
            booksRepository.save(existingBook);
        }
        catch (DataAccessException e){
            throw new RuntimeException("Database access error occurred while updating an existing book. " + e.getMessage());
        }
        catch (Exception e){
            throw new RuntimeException("An unexpected error occurred while updating an existing book. " + e.getMessage());
        }
    }

    @Transactional
    public void deleteBook(Integer bookId) {
        if(!booksRepository.existsById(bookId)){
            throw new ResourceNotFoundException("Book ID: " + bookId + " does not exist.");
        }
        try{
            booksRepository.deleteById(bookId);
        }
        catch (DataAccessException e){
            throw new RuntimeException("Database access error occurred while deleting an existing book. " + e.getMessage());
        }
        catch (Exception e){
            throw new RuntimeException("An unexpected error occurred while deleting an existing book. " + e.getMessage());
        }
    }

}



