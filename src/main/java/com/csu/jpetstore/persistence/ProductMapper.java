package com.csu.jpetstore.persistence;

import com.csu.jpetstore.domain.Product;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ProductMapper {
    List<Product> getProductListByCategory(String categoryId);
    Product getProduct(String productId);
    List<Product> searchProductList(String keywords);
    List<Product> getProductListWithItemsByCategory(String categoryId);
}