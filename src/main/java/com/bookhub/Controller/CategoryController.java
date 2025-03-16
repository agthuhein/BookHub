package com.bookhub.Controller;

import com.bookhub.Model.Categories;
import com.bookhub.Service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {

    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/getAllCategories")
    public ResponseEntity<Object> getAllCategories() {
        try{
            List<Categories> categories = categoryService.getAllCategories();
            return new ResponseEntity<>(categories, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>("An error occurred while fetching all the categories.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/api/admin/addCategory")
    public ResponseEntity<Object> addCategory(@Valid @RequestBody Categories categories, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid category data." + bindingResult.getAllErrors());
        }
        try{
            categoryService.addCategory(categories);
            return ResponseEntity.status(HttpStatus.CREATED).body("Successfully added category.");
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error adding category. " + e.getMessage());
        }
    }

    @PutMapping("/api/admin/updateCategory/{categoryId}")
    public ResponseEntity<Object> updateCategory(@PathVariable("categoryId") Integer categoryId,
                                                 @Valid @RequestBody Categories categories,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid category data." + bindingResult.getAllErrors());
        }
        try{
            categoryService.updateCategory(categoryId, categories);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully updated Category.");
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating category. " + e.getMessage());
        }
    }

    @DeleteMapping("/api/admin/deleteCategory/{categoryId}")
    public ResponseEntity<Object> deleteCategory(@PathVariable("categoryId") Integer categoryId) {
        try{
            categoryService.deleteCategory(categoryId);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted category.");
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting category. " + e.getMessage());
        }
    }
}
