package com.bookhub.Service;

import com.bookhub.Model.Users;
import com.bookhub.Repository.UserRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    private List<Users> usersList = new ArrayList<>();
    private UserRepository userRepository;
    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    //Get ALl Users
    public List<Users> getAllUsers() {
        try{
            return userRepository.findAll();
        }
        catch (DataAccessException e) {
            throw new RuntimeException("Database access error occurred while fetching all users.", e);
        } catch (Exception e) {
            throw new RuntimeException("An unexpected error occurred while fetching all users.", e);
        }

    }

    //Get Users By Role
    public List<Users> getUsersByRole(String role) {
        try{
            return userRepository.findByRole(role);
        }
        catch (DataAccessException e){
            throw new RuntimeException("Database access error occurred while fetching user by role.", e);
        }
        catch (Exception e){
            throw new RuntimeException("An unexpected error occurred while fetching user by role.", e);
        }
    }

    //Get Users By Id
//    public Optional<Product> getProductById(long id) {
//        return productsList.stream().filter(product -> product.getId() == id).findFirst();
//    }
    public Optional<Users> getUserById(Integer id) {
        try{
            return userRepository.findById(id);
        }
        catch (DataAccessException e){
            throw new RuntimeException("Database access error occurred while fetching user by ID.", e);
        }
        catch (Exception e){
            throw new RuntimeException("An unexpected error occurred while fetching user by ID.", e);
        }
    }
}
