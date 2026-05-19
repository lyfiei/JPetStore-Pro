package com.csu.jpetstore.mapper;

import com.csu.jpetstore.domain.Category;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CategoryMapper {
    List<Category> getCategoryList();
    Category getCategory(String categoryId);
}