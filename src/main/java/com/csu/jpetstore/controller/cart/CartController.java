package com.csu.jpetstore.controller.cart;

import com.csu.jpetstore.domain.Account;
import com.csu.jpetstore.domain.Cart;
import com.csu.jpetstore.domain.CartItem;
import com.csu.jpetstore.domain.Item;
import com.csu.jpetstore.service.CartService;
import com.csu.jpetstore.service.CatalogService;
import com.csu.jpetstore.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/view")
    public String viewCart(HttpSession session) {
        Account account = (Account) session.getAttribute("loginAccount");
        Cart cart = (Cart) session.getAttribute("cart");

        if (account == null) {
            if (cart == null) {
                cart = new Cart();
            }
        } else {
            java.util.List<CartItem> cartItems = cartService.getCartItemsByUserId(account.getUsername());
            if (cart == null) {
                cart = new Cart();
                for (CartItem item : cartItems) {
                    cart.addItem(item.getItem(), item.isInStock());
                    cart.setQuantityByItemId(item.getItem().getItemId(), item.getQuantity());
                }
            }
        }

        session.setAttribute("cart", cart);
        return "cart/cart";
    }

    @PostMapping("/add")
    public String addItem(@RequestParam("workingItemId") String workingItemId, HttpSession session) {
        Account account = (Account) session.getAttribute("loginAccount");
        Cart cart = (Cart) session.getAttribute("cart");

        if (account == null) {
            if (cart == null) {
                cart = new Cart();
            }
            CatalogService catalogService = new CatalogService();
            Item item = catalogService.getItem(workingItemId);
            cart.addItem(item, catalogService.isItemInStock(item.getItemId()));

            LogService logService = new LogService();
            logService.logAddToCart(session.getId(), item.getItemId(), 1);
        } else {
            if (cart == null) {
                cart = new Cart();
                java.util.List<CartItem> dbItems = cartService.getCartItemsByUserId(account.getUsername());
                for (CartItem item : dbItems) {
                    cart.addItem(item.getItem(), item.isInStock());
                    cart.setQuantityByItemId(item.getItem().getItemId(), item.getQuantity());
                }
            }
            cartService.addCartItem(cart, workingItemId, account.getUsername());

            LogService logService = new LogService();
            logService.logAddToCart(session.getId(), account.getUsername(), workingItemId, 1);
        }

        session.setAttribute("cart", cart);
        return "redirect:/cart/view";
    }

    @GetMapping("/remove")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> removeItem(@RequestParam("workingItemId") String workingItemId, HttpSession session) {
        Account account = (Account) session.getAttribute("loginAccount");
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null) {
            cart = new Cart();
        }

        LogService logService = new LogService();

        if (account == null) {
            cart.removeItemById(workingItemId);
            logService.removeFromCart(session.getId(), workingItemId);
        } else {
            cartService.removeCartItem(cart, account.getUsername(), workingItemId);
            logService.removeFromCart(session.getId(), account.getUsername(), workingItemId);
        }

        session.setAttribute("cart", cart);

        BigDecimal subTotal = cart.getSubTotal();
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
        String subTotalStr = currencyFormat.format(subTotal);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("subTotal", subTotalStr);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateCart(
            @RequestParam("itemId") String itemId,
            @RequestParam("quantity") String quantityStr,
            HttpSession session) {

        try {
            int quantity = Integer.parseInt(quantityStr);
            Account account = (Account) session.getAttribute("loginAccount");
            Cart cart = (Cart) session.getAttribute("cart");

            if (cart == null) {
                cart = new Cart();
            }

            LogService logService = new LogService();

            if (account == null) {
                cart.setQuantityByItemId(itemId, quantity);
                if (quantity < 1) {
                    cart.removeItemById(itemId);
                }
                logService.updateCart(session.getId(), quantity);
            } else {
                cartService.updateQuantity(cart, account.getUsername(), itemId, quantity);
                if (quantity < 1) {
                    cartService.removeCartItem(cart, account.getUsername(), itemId);
                }
                logService.updateCart(account.getUsername(), session.getId(), quantity);
            }

            session.setAttribute("cart", cart);

            BigDecimal subTotal = cart.getSubTotal();
            BigDecimal itemTotal = BigDecimal.ZERO;
            Iterator<CartItem> items = cart.getAllCartItems();
            while (items.hasNext()) {
                CartItem cartItem = items.next();
                if (cartItem.getItem().getItemId().equals(itemId)) {
                    itemTotal = cartItem.calculateTotal();
                    break;
                }
            }

            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
            String subTotalStr = currencyFormat.format(subTotal);
            String itemTotalStr = currencyFormat.format(itemTotal);

            Map<String, Object> response = new HashMap<>();
            response.put("itemTotal", itemTotalStr);
            response.put("subTotal", subTotalStr);

            return ResponseEntity.ok(response);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/update-ajax")
    @ResponseBody
    public String updateCartAjax(
            @RequestParam("itemId") String itemId,
            @RequestParam("quantity") int quantity,
            HttpSession session) {

        Account account = (Account) session.getAttribute("loginAccount");
        Cart cart = (Cart) session.getAttribute("cart");

        if (cart == null) {
            cart = new Cart();
        }

        if (account == null) {
            cart.setQuantityByItemId(itemId, quantity);
            if (quantity < 1) {
                cart.removeItemById(itemId);
            }
        } else {
            cartService.updateQuantity(cart, account.getUsername(), itemId, quantity);
            if (quantity < 1) {
                cartService.removeCartItem(cart, account.getUsername(), itemId);
            }
        }

        session.setAttribute("cart", cart);
        return "success";
    }
}
