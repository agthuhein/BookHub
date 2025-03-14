package com.bookhub.Controller;

import com.bookhub.CustomException.UnauthorizedActionException;
import com.bookhub.CustomException.UserNotFoundException;
import com.bookhub.Model.Users;
import com.bookhub.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/{userId}/update")
    public ResponseEntity<String> updateUser(@PathVariable("userId") Integer userId, @RequestBody Users updateUser) {
        if(userService.updateUser(userId, updateUser)) {
            return new ResponseEntity<>("User updated successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("User update failed", HttpStatus.BAD_REQUEST);

    }

}
