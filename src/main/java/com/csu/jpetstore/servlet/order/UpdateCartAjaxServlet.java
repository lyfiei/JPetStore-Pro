package com.csu.jpetstore.servlet.order;

import com.csu.jpetstore.domain.Account;
import com.csu.jpetstore.domain.Cart;
import com.csu.jpetstore.persistence.impl.CartDaoImpl;
import com.csu.jpetstore.service.CartService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class UpdateCartAjaxServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Cart cart = (Cart) session.getAttribute("cart");

        String itemId = req.getParameter("itemId");
        int quantity = Integer.parseInt(req.getParameter("quantity"));

        Account account = (Account) session.getAttribute("loginAccount");
        CartService cartService = new CartService();

        if (account == null) {
            cart.setQuantityByItemId(itemId, quantity);
            if (quantity < 1) cart.removeItemById(itemId);
        } else {
            cartService.updateQuantity(cart, account.getUsername(), itemId, quantity);
            if (quantity < 1) cartService.removeCartItem(cart, account.getUsername(), itemId);
        }

        resp.setContentType("text/plain");
        resp.getWriter().print("success");
    }
}
