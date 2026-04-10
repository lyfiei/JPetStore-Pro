package com.csu.jpetstore.servlet.cart;

import com.csu.jpetstore.domain.Account;
import com.csu.jpetstore.domain.Cart;
import com.csu.jpetstore.domain.CartItem;
import com.csu.jpetstore.domain.Item;
import com.csu.jpetstore.persistence.impl.CartDaoImpl;
import com.csu.jpetstore.service.CartService;
import com.csu.jpetstore.service.CatalogService;
import com.csu.jpetstore.service.LogService;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/addItemToCart")
public class AddItemToCartServlet extends HttpServlet {

    private static final String CART_FORM = "/WEB-INF/jsp/cart/cart.jsp";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String workingItemId = req.getParameter("workingItemId");

        HttpSession session = req.getSession();
        Cart cart = (Cart) session.getAttribute("cart");
        
        // 从 Spring 容器获取 Bean
        WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        CartService cartService = context.getBean(CartService.class);
        CatalogService catalogService = context.getBean(CatalogService.class);
        
        Account account = (Account) session.getAttribute("loginAccount");
        if (account == null) {
            //resp.sendRedirect("signonForm");
            //return;
            if (cart == null) {
                cart = new Cart();
            }
            Item item = catalogService.getItem(workingItemId);
            // 1. 内存 Cart 添加商品
            CartItem cartItem = cart.addItem(item, catalogService.isItemInStock(item.getItemId()));
            LogService logService = new LogService();
            logService.logAddToCart(session.getId(),item.getItemId(),1);
        }else{
            if (cart == null) {
                cart = new Cart();
                List<CartItem> dbItems = cartService.getCartItemsByUserId(account.getUsername());
                for (CartItem item : dbItems) {
                    cart.addItem(item.getItem(), item.isInStock());
                    cart.setQuantityByItemId(item.getItem().getItemId(), item.getQuantity());
                }
            }
            String userId = account.getUsername();
            cartService.addCartItem(cart, workingItemId,userId);
            LogService logService = new LogService();
            logService.logAddToCart(session.getId(),account.getUsername(),workingItemId,1);
        }

        session.setAttribute("cart", cart);
        resp.sendRedirect(req.getContextPath() + "/cartForm");
    }

    //@Override
    //protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    //    String workingItemId = req.getParameter("workingItemId");
    //
    //    HttpSession session = req.getSession();
    //    Cart cart = (Cart) session.getAttribute("cart");
    //
    //    if(cart == null) {
    //        cart = new Cart();
    //    }
    //
    //    if (cart.containsItemId(workingItemId)) {
    //        cart.incrementQuantityByItemId(workingItemId);
    //    } else {
    //        CatalogService catalogService = new CatalogService();
    //        boolean isInStock = catalogService.isItemInStock(workingItemId);
    //        Item item = catalogService.getItem(workingItemId);
    //        cart.addItem(item, isInStock);
    //
    //    }
    //
    //    session.setAttribute("cart", cart);
    //    req.getRequestDispatcher(CART_FORM).forward(req, resp);
    //}

    //@Override
    //protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    //    String workingItemId = req.getParameter("workingItemId");
    //
    //    HttpSession session = req.getSession();
    //    Cart cart = (Cart) session.getAttribute("cart");
    //
    //    if(cart == null) {
    //        cart = new Cart();
    //    }
    //
    //    if (cart.containsItemId(workingItemId)) {
    //        cart.incrementQuantityByItemId(workingItemId);
    //    } else {
    //        CatalogService catalogService = new CatalogService();
    //        boolean isInStock = catalogService.isItemInStock(workingItemId);
    //        Item item = catalogService.getItem(workingItemId);
    //        cart.addItem(item, isInStock);
    //
    //    }
    //
    //    session.setAttribute("cart", cart);
    //    resp.sendRedirect(req.getContextPath() + "/cartForm");
    //}
}
