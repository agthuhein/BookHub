package com.bookhub.Service;

import com.bookhub.Model.Categories;
import com.bookhub.Repository.MySQL.CategoriesRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {
    private final CategoriesRepository categoriesRepository;
    public CategoryService(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    //Get all categories
    @Transactional(readOnly = true)
    public List<Categories> getAllCategories(){
        try {
            return categoriesRepository.findAll();
        }
        catch (DataAccessException e){
            throw new RuntimeException("Database access error occurred while fetching all categories." + e);
        }
        catch (Exception e){
            throw new RuntimeException("An unexpected error occurred while fetching all categories.", e);
        }
    }

    //Add new category
    @Transactional
    public void addCategory(Categories categories){
        if(categories.getCategoryName() != null && !categories.getCategoryName().isEmpty()){
            if(categoriesRepository.existsByCategoryName(categories.getCategoryName())){
                throw new RuntimeException("Category name already exists.");
            }
            try{
                categoriesRepository.save(categories);
            }
            catch (DataAccessException e){
                throw new RuntimeException("Database access error occurred while adding category." + e);
            }
            catch (Exception e){
                throw new RuntimeException("An unexpected error occurred while adding category." + e);
            }
        }
        else{
            throw new RuntimeException("Category name is empty.");
        }
    }

    //Update existing category
    @Transactional
    public void updateCategory(Integer categoryId, Categories updatedCategories){
        if(updatedCategories.getCategoryName() != null && !updatedCategories.getCategoryName().isEmpty()){
            if(!categoriesRepository.existsById(categoryId)){
                throw new RuntimeException("Category ID: " + categoryId + " does not exist.");
            }
            Categories categories = categoriesRepository.findById(categoryId).get();
            categories.setCategoryName(updatedCategories.getCategoryName());
            try{
                categoriesRepository.save(categories);
            }
            catch (DataAccessException e){
                throw new RuntimeException("Database access error occurred while updating category." + e);
            }
            catch (Exception e){
                throw new RuntimeException("An unexpected error occurred while updating category." + e);
            }
        }
        else{
            throw new RuntimeException("Category name is empty.");
        }
    }

    //Delete category
    @Transactional
    public void deleteCategory(Integer categoryId){
        if(!categoriesRepository.existsById(categoryId)){
            throw new RuntimeException("Category ID: " + categoryId + " does not exist.");
        }
        try{
            categoriesRepository.deleteById(categoryId);
        }
        catch (DataAccessException e){
            throw new RuntimeException("Database access error occurred while deleting category." + e);
        }
        catch (Exception e){
            throw new RuntimeException("An unexpected error occurred while deleting category." + e);
        }
    }
}
