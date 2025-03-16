package com.bookhub.Service;

import com.bookhub.Model.Authors;
import com.bookhub.Repository.AuthorsRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {
    private final AuthorsRepository authorRepository;
    public AuthorService(AuthorsRepository authorRepository) {
        this.authorRepository = authorRepository;
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
                throw new RuntimeException("Author ID: " + authorId + "does not exist!");
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
            throw new RuntimeException("Author ID: " + authorId + "does not exist!");
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
