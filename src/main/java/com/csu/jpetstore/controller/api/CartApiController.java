package com.csu.jpetstore.controller.api;

import com.csu.jpetstore.domain.Cart;
import com.csu.jpetstore.domain.CartItem;
import com.csu.jpetstore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 购物车 RESTful API Controller
 * 路径：/api/v1/cart/**
 */
@RestController
@RequestMapping("/api/v1/cart")
public class CartApiController {

    @Autowired
    private CartService cartService;

    /**
     * 查看购物车
     * GET /api/v1/cart
     */
    @GetMapping
    public Map<String, Object> viewCart(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");

        Cart cart = new Cart();
        if (username != null) {
            List<CartItem> dbItems = cartService.getCartItemsByUserId(username);
            for (CartItem item : dbItems) {
                cart.addItem(item.getItem(), item.isInStock());
                cart.setQuantityByItemId(item.getItem().getItemId(), item.getQuantity());
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "success");
        result.put("data", buildCartData(cart));

        return result;
    }

    /**
     * 添加商品到购物车
     * POST /api/v1/cart/items
     */
    @PostMapping("/items")
    public Map<String, Object> addItem(
            @RequestBody Map<String, Object> body,
            HttpServletRequest request) {

        String username = (String) request.getAttribute("username");
        String itemId = (String) body.get("itemId");
        int quantity = body.get("quantity") != null ? ((Number) body.get("quantity")).intValue() : 1;

        Cart cart = new Cart();
        List<CartItem> dbItems = cartService.getCartItemsByUserId(username);
        for (CartItem ci : dbItems) {
            cart.addItem(ci.getItem(), ci.isInStock());
            cart.setQuantityByItemId(ci.getItem().getItemId(), ci.getQuantity());
        }

        CartItem existingItem = cartService.getCartItem(username, itemId);
        if (existingItem != null) {
            cartService.updateQuantity(cart, username, itemId, existingItem.getQuantity() + quantity);
        } else {
            cartService.addCartItem(cart, itemId, username);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "添加成功");

        return result;
    }

    /**
     * 更新购物车商品数量
     * PUT /api/v1/cart/items/{itemId}
     */
    @PutMapping("/items/{itemId}")
    public Map<String, Object> updateItem(
            @PathVariable String itemId,
            @RequestBody Map<String, Object> body,
            HttpServletRequest request) {

        String username = (String) request.getAttribute("username");
        int quantity = body.get("quantity") != null ? ((Number) body.get("quantity")).intValue() : 1;

        Cart cart = new Cart();
        List<CartItem> dbItems = cartService.getCartItemsByUserId(username);
        for (CartItem ci : dbItems) {
            cart.addItem(ci.getItem(), ci.isInStock());
            cart.setQuantityByItemId(ci.getItem().getItemId(), ci.getQuantity());
        }

        cartService.updateQuantity(cart, username, itemId, quantity);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "更新成功");

        return result;
    }

    /**
     * 删除购物车商品
     * DELETE /api/v1/cart/items/{itemId}
     */
    @DeleteMapping("/items/{itemId}")
    public Map<String, Object> removeItem(
            @PathVariable String itemId,
            HttpServletRequest request) {

        String username = (String) request.getAttribute("username");

        Cart cart = new Cart();
        List<CartItem> dbItems = cartService.getCartItemsByUserId(username);
        for (CartItem ci : dbItems) {
            cart.addItem(ci.getItem(), ci.isInStock());
            cart.setQuantityByItemId(ci.getItem().getItemId(), ci.getQuantity());
        }

        cartService.removeCartItem(cart, username, itemId);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "删除成功");

        return result;
    }

    /**
     * 清空购物车
     * DELETE /api/v1/cart
     */
    @DeleteMapping
    public Map<String, Object> clearCart(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");

        if (username != null) {
            cartService.clearCart(username);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "购物车已清空");

        return result;
    }

    /**
     * 将 Cart 对象转换为前端期望的扁平格式
     */
    private Map<String, Object> buildCartData(Cart cart) {
        Map<String, Object> data = new HashMap<>();
        List<Map<String, Object>> cartItems = new ArrayList<>();

        for (CartItem ci : cart.getCartItemList()) {
            Map<String, Object> itemData = new HashMap<>();
            itemData.put("itemId", ci.getItem().getItemId());
            itemData.put("productId", ci.getItem().getProductId());
            itemData.put("productName", ci.getItem().getProduct() != null ? ci.getItem().getProduct().getName() : "");
            itemData.put("description", ci.getItem().getProduct() != null ? ci.getItem().getProduct().getDescription() : "");
            itemData.put("listPrice", ci.getItem().getListPrice());
            itemData.put("quantity", ci.getQuantity());
            itemData.put("inStock", ci.isInStock());
            cartItems.add(itemData);
        }

        data.put("cartItems", cartItems);
        return data;
    }
}
