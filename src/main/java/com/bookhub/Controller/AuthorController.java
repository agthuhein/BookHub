package com.bookhub.Controller;

import com.bookhub.Model.Authors;
import com.bookhub.Service.AuthorService;
import org.hibernate.type.descriptor.java.ObjectJavaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuthorController {

    private AuthorService authorService;
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/getAllAuthors")
    public ResponseEntity<Object> getAllAuthors() {
        try{
            List<Authors> authors = authorService.getAllAuthors();
            return new ResponseEntity<>(authors, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>("An error occurred while fetching all the authors.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
