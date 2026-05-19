package com.csu.jpetstore.controller.api;

import com.csu.jpetstore.domain.Account;
import com.csu.jpetstore.domain.Cart;
import com.csu.jpetstore.domain.CartItem;
import com.csu.jpetstore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
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
    public Map<String, Object> viewCart(HttpSession session) {
        Account account = (Account) session.getAttribute("loginAccount");
        Cart cart = (Cart) session.getAttribute("cart");

        // TODO: 返回统一响应格式
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "success");
        result.put("data", cart);

        return result;
    }

    /**
     * 添加商品到购物车
     * POST /api/v1/cart/items
     */
    @PostMapping("/items")
    public Map<String, Object> addItem(
            @RequestParam String itemId,
            @RequestParam(defaultValue = "1") int quantity,
            HttpSession session) {
        
        Account account = (Account) session.getAttribute("loginAccount");
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null) {
            cart = new Cart();
        }

        // TODO: 调用 CartService 添加商品
        // cartService.addCartItem(cart, itemId, account.getUsername());

        session.setAttribute("cart", cart);

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
            @RequestParam int quantity,
            HttpSession session) {

        Account account = (Account) session.getAttribute("loginAccount");
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null) {
            cart = new Cart();
        }

        // TODO: 调用 CartService 更新数量
        // cartService.updateQuantity(cart, account.getUsername(), itemId, quantity);

        session.setAttribute("cart", cart);

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
            HttpSession session) {

        Account account = (Account) session.getAttribute("loginAccount");
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null) {
            cart = new Cart();
        }

        // TODO: 调用 CartService 删除商品
        // cartService.removeCartItem(cart, account.getUsername(), itemId);

        session.setAttribute("cart", cart);

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
    public Map<String, Object> clearCart(HttpSession session) {
        Account account = (Account) session.getAttribute("loginAccount");

        if (account != null) {
            // TODO: 调用 CartService 清空购物车
            // cartService.clearCart(account.getUsername());
        }

        session.removeAttribute("cart");

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "购物车已清空");

        return result;
    }
}
