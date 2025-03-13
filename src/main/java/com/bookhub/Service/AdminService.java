package com.bookhub.Service;

import com.bookhub.Model.Users;
import com.bookhub.Repository.UserRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    private UserRepository userRepository;
    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    //getALlUser
    public List<Users> getAllUsers() {
        try{
            return userRepository.findAll();
        }
        catch (DataAccessException e) {
            throw new RuntimeException("Database access error occurred while fetching users.", e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while fetching users.", e);
        }

    }
}
