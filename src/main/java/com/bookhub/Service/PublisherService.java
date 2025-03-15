package com.bookhub.Service;

import com.bookhub.Model.Publishers;
import com.bookhub.Repository.PublisherRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PublisherService {
    private final PublisherRepository publisherRepository;
    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    //Get all publishers
    public List<Publishers> getAllPublishers() {
        try{
            return publisherRepository.findAll();
        }
        catch (DataAccessException e){
            throw new RuntimeException("Database access error occurred while fetching all authors." + e);
        }
        catch (Exception e){
            throw new RuntimeException("An unexpected error occurred while fetching all authors.", e);
        }
    }
}
