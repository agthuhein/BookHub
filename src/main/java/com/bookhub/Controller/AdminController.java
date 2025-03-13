package com.bookhub.Controller;

import com.bookhub.Model.Users;
import com.bookhub.Service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
