package com.csu.jpetstore.controller;

import com.csu.jpetstore.domain.Account;
import com.csu.jpetstore.domain.Category;
import com.csu.jpetstore.domain.Item;
import com.csu.jpetstore.domain.Product;
import com.csu.jpetstore.service.CatalogService;
import com.csu.jpetstore.service.LogService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Controller
@RequestMapping("/catalog")
public class CatalogController {
    @Autowired
    private CatalogService catalogService;

//    @Autowired
//    private LogService logService;
    @GetMapping("/main")
    public String viewMain() {
        return "catalog/main";
    }
    @PostMapping("/main")
    public String postMain() {
        return viewMain();
    }

    @GetMapping("/category")
    public String viewCategory(@RequestParam("categoryId") String categoryId,
                               Model model,
                               HttpSession session) {

        // 1. 调用业务层获取数据
        Category category = catalogService.getCategory(categoryId);
        List<Product> productList = catalogService.getProductListByCategory(categoryId);

        // 2. 将数据存入 Model (供 Thymeleaf 渲染)
        // 原 Servlet 存入了 Session，但在 Spring MVC 中，
        // 除非数据跨页面需要，通常建议存入 Model 以减少内存压力。
        model.addAttribute("category", category);
        model.addAttribute("productList", productList);

        // 3. 记录日志 (保持原 Servlet 逻辑)
//        Account account = (Account) session.getAttribute("account");
//        if (account != null) {
//            logService.logClickButton(session.getId(), account.getUsername(), "Category", categoryId);
//        } else {
//            logService.logClickButton(session.getId(), "Category", categoryId);
//        }

        // 4. 返回视图名称：templates/catalog/category.html
        return "catalog/category";
    }

    @GetMapping("/product")
    public String viewProduct(@RequestParam("productId") String productId,
                              Model model,
                              HttpSession session) {

        // 1. 调用业务层获取数据
        Product product = catalogService.getProduct(productId);
        List<Item> itemList = catalogService.getItemListByProduct(productId);

        // 2. 将数据存入 Model (供 Thymeleaf 渲染)
        // 原 Servlet 存入了 Session，但在 Spring MVC 中，
        // 除非数据跨页面需要，通常建议存入 Model 以减少内存压力。
        model.addAttribute("product", product);
        model.addAttribute("itemList", itemList);

        // 3. 记录日志 (保持原 Servlet 逻辑)
//        Account account = (Account) session.getAttribute("account");
//        if (account != null) {
//            logService.logClickButton(session.getId(), account.getUsername(), "Product", productId);
//        } else {
//            logService.logClickButton(session.getId(), "Product", productId);
//        }

        // 4. 返回视图名称：templates/catalog/product.html
        return "catalog/product";
    }

    @GetMapping("/item")
    public String viewItem(@RequestParam("itemId") String itemId,
                           Model model,
                           HttpSession session) {
        // 1. 调用业务层获取数据
        Item item = catalogService.getItem(itemId);
        Product product = item.getProduct();
        // 2. 将数据存入 Model (供 Thymeleaf 渲染)
        // 原 Servlet 存入了 Session，但在 Spring MVC 中，
        // 除非数据跨页面需要，通常建议存入 Model 以减少内存压力。
        model.addAttribute("item", item);
        model.addAttribute("product", product);
        // 3. 记录日志 (保持原 Servlet 逻辑)
//        Account account = (Account) session.getAttribute("account");
//        if (account != null) {
//            logService.logClickButton(session.getId(), account.getUsername(), "Item", itemId);
//        } else {
//            logService.logClickButton(session.getId(), "Item", itemId);
//        }
        // 4. 返回视图名称：templates/catalog/item.html
        return "catalog/item";
    }

    @RequestMapping("/search")
    public String searchProducts(@RequestParam(value = "keyword", required = false) String keyword,
                                 Model model,
                                 HttpSession session) throws UnsupportedEncodingException {

        // 1. 校验关键字
        if (keyword == null || keyword.trim().isEmpty()) {
            // 原 Servlet 逻辑：如果为空则不处理或返回。
            // 这里建议根据业务需求返回主页或报错提示
            return "redirect:/catalog/main";
        }

        // 2. 记录搜索日志 (保持原 Servlet 逻辑)
//        Account account = (Account) session.getAttribute("account");
//        if (account != null) {
//            logService.search(session.getId(), account.getUsername(), keyword);
//        } else {
//            logService.search(session.getId(), keyword);
//        }

        // 3. 调用 Service 层获取数据
        List<Product> productList = catalogService.searchProductList(keyword);

        // 4. 将结果存入 Model (对应 request.setAttribute)
        model.addAttribute("productList", productList);
        model.addAttribute("keyword", keyword);

        // 5. 处理 "上次搜索 URL" 逻辑并存入 Session
        // Spring MVC 默认会处理编码，但为了拼接 URL，我们手动 encode 一下参数
        String encoded = URLEncoder.encode(keyword, "UTF-8");
        String lastSearchUrl = "catalog/search?keyword=" + encoded;

        session.setAttribute("lastSearchUrl", lastSearchUrl);
        session.setAttribute("lastSearchKeyword", keyword);

        // 6. 返回视图名称：templates/catalog/searchResult.html
        return "catalog/searchResult";
    }

    @GetMapping("/autoComplete")
    @ResponseBody // 关键点：直接将 List 转换为 JSON 写入响应体
    public List<Product> autoComplete(@RequestParam("keyword") String keyword) {
        // 1. 调用 Service 获取数据
        List<Product> productList = catalogService.searchProductList(keyword);

        // 2. 打印日志（对应原 Servlet 的 System.out）
        // 建议在生产环境使用 log.info
//        System.out.println("Auto-complete search for: " + keyword);

        // 3. 直接返回 List 对象
        // Spring MVC 会根据 Content-Type 自动处理成 JSON
        return productList;
    }

//    @GetMapping("/categoryAjax")
//    @ResponseBody
//    public List<Product> getCategoryAjax(@RequestParam("categoryId") String categoryId) {
//        // 1. 记录开始时间 (模拟原 Servlet 的耗时监控)
//        long start = System.currentTimeMillis();
//
//        // 2. 获取该分类下的产品列表
//        List<Product> productList = catalogService.getProductListByCategory(categoryId);
//
//        // 3. 核心逻辑：为每个 Product 加载其 Item 列表
//        // 注意：确保你的 Product 类中有一个 List<Item> items 属性
//        for (Product product : productList) {
//            List<Item> itemList = catalogService.getItemListByProduct(product.getProductId());
//            product.setItemList(itemList); // 将查出的 items 塞进 product 对象中
//        }
//
//        System.out.println("Category AJAX 总耗时: " + (System.currentTimeMillis() - start) + "ms");
//
//        // 4. 直接返回 List。Spring 会自动递归地把 Product 和它里面的 ItemList 转成嵌套 JSON
//        return productList;
//    }
    @GetMapping("/categoryAjax")
    @ResponseBody
    public List<Product> getCategoryAjax(@RequestParam("categoryId") String categoryId) {
        // 直接调用 Mapper 层的关联查询方法
        // MyBatis 会根据上面的 ResultMap 自动把数据封装进 Product 的 itemList 中
        List<Product> productList = catalogService.getProductListWithItemsByCategory(categoryId);

        return productList;
        // Spring 会自动将其序列化为如下格式：
        // [{"productId":"xxx", "name":"xxx", "itemList":[{"itemId":"xxx", "attribute1":"xxx"}, ...]}, ...]
    }

}
