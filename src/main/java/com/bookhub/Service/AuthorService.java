package com.bookhub.Service;

import com.bookhub.CustomException.CustomDeletionException;
import com.bookhub.Model.Authors;
import com.bookhub.Repository.MySQL.AuthorsRepository;
import com.bookhub.Repository.MySQL.BooksRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthorService {
    private final AuthorsRepository authorRepository;
    private final BooksRepository booksRepository;
    public AuthorService(AuthorsRepository authorRepository, BooksRepository booksRepository) {
        this.authorRepository = authorRepository;
        this.booksRepository = booksRepository;
    }
    //Get All Authors
    public List<Authors> getAllAuthors() {
        try{
            return authorRepository.findAll();
        }
        catch (DataAccessException e){
            throw new RuntimeException("Database access error occurred while fetching all authors.", e);
        }
        catch (Exception e){
            throw new RuntimeException("An unexpected error occurred while fetching all authors.", e);
        }
    }
    //Add new author
    public void addAuthor(Authors author) {
        if(author.getAuthorName() != null && !author.getAuthorName().isEmpty()) {

            //Checking if the author is already exist
            if(authorRepository.existsByAuthorName(author.getAuthorName())) {
                throw new RuntimeException("Author name already exists!");
            }
            try{
                authorRepository.save(author);
            }
            catch (DataAccessException e){
                throw new RuntimeException("Database access error occurred while adding author.", e);
            }
            catch (Exception e){
                throw new RuntimeException("An unexpected error occurred while adding author.", e);
            }
        }
        else{
            throw new RuntimeException("Author name cannot be empty!");
        }
    }
    //Update existing author
    public void updateAuthor(Integer authorId, Authors updatedAuthor) {
        if(updatedAuthor.getAuthorName() != null && !updatedAuthor.getAuthorName().isEmpty()) {
            if (!authorRepository.existsById(authorId)) {
                throw new RuntimeException("Author ID: " + authorId + " does not exist!");
            }
            Authors author = authorRepository.findById(authorId).get();
            author.setAuthorName(updatedAuthor.getAuthorName());
            try{
                authorRepository.save(author);
            }
            catch (DataAccessException e){
                throw new RuntimeException("Database access error occurred while updating author.", e);
            }
            catch (Exception e){
                throw new RuntimeException("An unexpected error occurred while updating author.", e);
            }
        }
        else{
            throw new RuntimeException("Author name cannot be empty!");
        }

    }
    //Delete author
    public void deleteAuthor(Integer authorId) {
        if(!authorRepository.existsById(authorId)) {
            throw new RuntimeException("Author ID: " + authorId + " does not exist!");
        }

        Authors author = authorRepository.findById(authorId)
                .orElseThrow(() -> new CustomDeletionException("Author not found"));

        if(booksRepository.existsByAuthors(authorId)){
            throw new CustomDeletionException("Cannot delete author. Books are still associated with the author.");
        }

        try{
            authorRepository.deleteById(authorId);
        }
        catch (DataAccessException e){
            throw new RuntimeException("Database access error occurred while deleting author.", e);
        }
        catch (Exception e){
            throw new RuntimeException("An unexpected error occurred while deleting author.", e);
        }
    }
}
