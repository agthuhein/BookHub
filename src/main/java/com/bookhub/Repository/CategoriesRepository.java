package com.bookhub.Repository;

import com.bookhub.Model.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Integer> {
    boolean existsByCategoryName(String categoryName);
}
