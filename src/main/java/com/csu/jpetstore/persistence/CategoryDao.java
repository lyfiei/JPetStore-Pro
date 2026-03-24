package com.csu.jpetstore.persistence;

import com.csu.jpetstore.domain.Category;

import java.util.List;

public interface CategoryDao {
    List<Category> getCategoryList();

    Category getCategory(String categoryId);
}
