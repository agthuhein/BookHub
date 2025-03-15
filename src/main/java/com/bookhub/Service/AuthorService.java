package com.bookhub.Service;

import com.bookhub.Model.Authors;
import com.bookhub.Model.Users;
import com.bookhub.Repository.AuthorRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    public AuthorService(AuthorRepository authorRepository) {
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
}
