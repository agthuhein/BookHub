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

    //Add new publisher
    public void addPublisher(Publishers publisher) {
        if(publisher.getPublisherName() != null && !publisher.getPublisherName().isEmpty()){

            if(publisherRepository.existsByPublisherName(publisher.getPublisherName())){
                throw new RuntimeException("Publisher name already exists!" );
            }
            try{
                publisherRepository.save(publisher);
            }
            catch (DataAccessException e){
                throw new RuntimeException("Database access error occurred while adding publisher." + e);
            }
            catch (Exception e){
                throw new RuntimeException("An unexpected error occurred while adding publisher." + e);
            }
        }
        else{
            throw new RuntimeException("Publisher name is empty!" );
        }
    }

    //Update publisher
    public void updatePublisher(Integer publisherId, Publishers updatePublisher) {
        if(updatePublisher.getPublisherName() != null && !updatePublisher.getPublisherName().isEmpty()){
            if(!publisherRepository.existsById(publisherId)){
                throw new RuntimeException("Publisher ID: " + publisherId + " does not exist!");
            }
            Publishers publisher = publisherRepository.findById(publisherId).get();
            publisher.setPublisherName(updatePublisher.getPublisherName());
            try{
                publisherRepository.save(publisher);
            }
            catch (DataAccessException e){
                throw new RuntimeException("Database access error occurred while updating publisher." + e);
            }
            catch (Exception e){
                throw new RuntimeException("An unexpected error occurred while updating publisher." + e);
            }
        }
        else{
            throw new RuntimeException("Publisher name is empty!" );
        }
    }

    //delete publisher
    public void deletePublisher(Integer publisherId) {
        if(!publisherRepository.existsById(publisherId)){
            throw new RuntimeException("Publisher ID: " + publisherId + " does not exist!");
        }
        try{
            publisherRepository.deleteById(publisherId);
        }
        catch (DataAccessException e){
            throw new RuntimeException("Database access error occurred while deleting publisher." + e);
        }
        catch (Exception e){
            throw new RuntimeException("An unexpected error occurred while deleting publisher." + e);
        }
    }
}
