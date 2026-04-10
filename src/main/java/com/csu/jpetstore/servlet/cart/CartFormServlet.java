package com.csu.jpetstore.servlet.cart;

import com.csu.jpetstore.domain.Account;
import com.csu.jpetstore.domain.Cart;
import com.csu.jpetstore.domain.CartItem;
import com.csu.jpetstore.persistence.impl.CartDaoImpl;
import com.csu.jpetstore.service.CartService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class CartFormServlet extends HttpServlet {
    private String CART_FORM = "/WEB-INF/jsp/cart/cart.jsp";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        Account account = (Account) req.getSession().getAttribute("loginAccount");
        Cart cart = (Cart) session.getAttribute("cart");
        if (account == null) {

            if(cart == null) {
                cart = new Cart();
            }
        }else{
            CartService cartService = new CartService();
            // 从数据库取该用户的购物车
            List<CartItem> cartItems = cartService.getCartItemsByUserId(account.getUsername());
            // 计算小计和数量
            if(cart == null) {
                cart = new Cart();
                for (CartItem item : cartItems) {
                    cart.addItem(item.getItem(), item.isInStock());
                    cart.setQuantityByItemId(item.getItem().getItemId(), item.getQuantity());
                }
            }
        }


        req.getSession().setAttribute("cart", cart);
        req.getRequestDispatcher(CART_FORM).forward(req, resp);
    }
}
