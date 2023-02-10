package com.example.alleywayalliancelms.Repository;

import com.example.alleywayalliancelms.Model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findCategoryByCategoryName(String cName);
}