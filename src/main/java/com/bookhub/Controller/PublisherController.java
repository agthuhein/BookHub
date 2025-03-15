package com.bookhub.Controller;

import com.bookhub.Model.Publishers;
import com.bookhub.Service.PublisherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
