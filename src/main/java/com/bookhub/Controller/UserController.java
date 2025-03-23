package com.bookhub.Controller;

import com.bookhub.Model.Users;
import com.bookhub.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/updateUser/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable("userId") Integer userId, @RequestBody Users updateUser) {
        if(userService.updateUser(userId, updateUser)) {
            return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("User update failed", HttpStatus.BAD_REQUEST);

    }

}
