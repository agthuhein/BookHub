package com.bookhub.Controller;

import com.bookhub.Model.Users;
import com.bookhub.Service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;
    public AdminController(AdminService adminService) {

        this.adminService = adminService;
    }
    @GetMapping("/getUsers")
    public ResponseEntity<Object> getAllUser() {
        try{
            List<Users> users = adminService.getAllUsers();
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>("An error occurred while fetching all the users.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/getUsersByRole")
    public ResponseEntity<Object> getUsersByRole(@RequestParam(required = false) String role)
    {
        try{
            List<Users> l_users = adminService.getUsersByRole(role);
            Optional<Users> users = l_users.stream().filter(u -> u.getRole().equals(role)).findFirst();
            if (users.isPresent()) {
                return new ResponseEntity<>(users.get(), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("No such role", HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e){
            return new ResponseEntity<>("An error occurred while fetching the users by role.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/getUsersById/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable Integer userId) {
        try{
            Optional user = adminService.getUserById(userId)
                    .stream().filter(u -> u.getUserId().equals(userId)).findFirst();
            if (user.isPresent()) {
                return new ResponseEntity<>(user.get(), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>("No such user", HttpStatus.NOT_FOUND);
            }

        }
        catch (Exception e){
            return new ResponseEntity<>("An error occurred while fetching the users by id.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
