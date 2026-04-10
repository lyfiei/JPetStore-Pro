package com.csu.jpetstore.service;

import com.csu.jpetstore.domain.Category;
import com.csu.jpetstore.domain.Item;
import com.csu.jpetstore.domain.Product;
import com.csu.jpetstore.persistence.CategoryMapper;
import com.csu.jpetstore.persistence.ItemMapper;
import com.csu.jpetstore.persistence.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // 核心：告诉 Spring 这是一个业务逻辑类，方便 Controller 自动注入
public class CatalogService {

    // 使用 Autowired 注入接口，Spring 会自动寻找 MyBatis 生成的代理实现类
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ItemMapper itemMapper;

    // 删掉原来的构造函数 CatalogService()，Spring 会自动处理依赖

    public List<Category> getCategoryList() {
        return categoryMapper.getCategoryList();
    }

    public Category getCategory(String categoryId) {
        return categoryMapper.getCategory(categoryId);
    }

    public Product getProduct(String productId) {
        return productMapper.getProduct(productId);
    }

    public List<Product> getProductListByCategory(String categoryId) {
        return productMapper.getProductListByCategory(categoryId);
    }

    public List<Product> searchProductList(String keyword) {
        // 在XML中处理模糊查询。
        return productMapper.searchProductList( keyword.toLowerCase() );
    }

    public List<Item> getItemListByProduct(String productId) {
        return itemMapper.getItemListByProduct(productId);
    }

    public Item getItem(String itemId) {
        return itemMapper.getItem(itemId);
    }

    public boolean isItemInStock(String itemId) {
        return itemMapper.getInventoryQuantity(itemId) > 0;
    }

    public List<Product> getProductListWithItemsByCategory(String categoryId) {
        return productMapper.getProductListWithItemsByCategory(categoryId);
    }
}