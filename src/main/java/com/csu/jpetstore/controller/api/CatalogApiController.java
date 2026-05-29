package com.csu.jpetstore.controller.api;

import com.csu.jpetstore.common.BusinessException;
import com.csu.jpetstore.common.Result;
import com.csu.jpetstore.domain.Category;
import com.csu.jpetstore.domain.Item;
import com.csu.jpetstore.domain.Product;
import com.csu.jpetstore.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class CatalogApiController {

    @Autowired
    private CatalogService catalogService;

    @GetMapping("/categories")
    public Result<List<Category>> getCategories() {
        return Result.success(catalogService.getCategoryList());
    }

    @GetMapping("/categories/{categoryId}")
    public Result<Map<String, Object>> getCategoryWithProducts(@PathVariable String categoryId) {
        Category category = catalogService.getCategory(categoryId);
        if (category == null) {
            throw new BusinessException("分类不存在: " + categoryId);
        }
        List<Product> products = catalogService.getProductListByCategory(categoryId);
        Map<String, Object> data = new HashMap<>();
        data.put("category", category);
        data.put("products", products);
        return Result.success(data);
    }

    @GetMapping("/categories/{categoryId}/products-with-items")
    public Result<List<Product>> getCategoryProductsWithItems(@PathVariable String categoryId) {
        return Result.success(catalogService.getProductListWithItemsByCategory(categoryId));
    }

    @GetMapping("/products/{productId}")
    public Result<Map<String, Object>> getProductDetail(@PathVariable String productId) {
        Product product = catalogService.getProduct(productId);
        if (product == null) {
            throw new BusinessException("商品不存在: " + productId);
        }
        List<Item> items = catalogService.getItemListByProduct(productId);
        Map<String, Object> data = new HashMap<>();
        data.put("product", product);
        data.put("items", items);
        return Result.success(data);
    }

    @GetMapping("/items/{itemId}")
    public Result<Item> getItemDetail(@PathVariable String itemId) {
        Item item = catalogService.getItem(itemId);
        if (item == null) {
            throw new BusinessException("规格项不存在: " + itemId);
        }
        return Result.success(item);
    }

    @GetMapping("/products/search")
    public Result<List<Product>> searchProducts(@RequestParam String keyword) {
        return Result.success(catalogService.searchProductList(keyword));
    }

    @GetMapping("/products/autocomplete")
    public Result<List<Product>> autocompleteProducts(@RequestParam String keyword) {
        return Result.success(catalogService.searchProductList(keyword));
    }
}
