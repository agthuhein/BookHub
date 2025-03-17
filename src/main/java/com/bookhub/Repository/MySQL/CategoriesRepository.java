package com.bookhub.Repository.MySQL;

import com.bookhub.Model.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Integer> {
    boolean existsByCategoryName(String categoryName);
}
