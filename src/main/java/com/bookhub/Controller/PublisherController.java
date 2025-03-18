package com.bookhub.Controller;

import com.bookhub.CustomException.CustomDeletionException;
import com.bookhub.Model.Publishers;
import com.bookhub.Service.PublisherService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PublisherController {

    private final PublisherService publisherService;
    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GetMapping("/getAllPublishers")
    public ResponseEntity<Object> getAllPublishers() {
        try{
            List<Publishers> publishers = publisherService.getAllPublishers();
            return new ResponseEntity<>(publishers, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>("An error occurred while fetching all the authors.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/api/admin/addPublisher")
    public ResponseEntity<Object> addPublisher(@Valid @RequestBody Publishers publisher, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid publisher data." + bindingResult.getAllErrors());
        }
        try{
            publisherService.addPublisher(publisher);
            return ResponseEntity.status(HttpStatus.CREATED).body("Successfully added publisher.");
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding publisher. " + e.getMessage());
        }
    }

    @PutMapping("/api/admin/updatePublisher/{publisherId}")
    public ResponseEntity<Object> updatePublisher(@PathVariable("publisherId") Integer publisherId,
                                                  @Valid @RequestBody Publishers publisher,
                                                  BindingResult result){
        if(result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid publisher data." + result.getAllErrors());
        }
        try {
            publisherService.updatePublisher(publisherId, publisher);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully updated publisher.");
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating publisher. " + e.getMessage());
        }
    }

    @DeleteMapping("/api/admin/deletePublisher/{publisherId}")
    public ResponseEntity<Object> deletePublisher(@PathVariable("publisherId") Integer publisherId) {
        try {
            publisherService.deletePublisher(publisherId);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted publisher.");
        }
        catch (CustomDeletionException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting publisher. " + e.getMessage());
        }
    }
}
