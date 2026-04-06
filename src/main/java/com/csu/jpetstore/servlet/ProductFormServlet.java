package com.csu.jpetstore.servlet;

import com.csu.jpetstore.domain.Account;
import com.csu.jpetstore.domain.Item;
import com.csu.jpetstore.domain.Product;
import com.csu.jpetstore.service.CatalogService;
import com.csu.jpetstore.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Component
public class ProductFormServlet extends HttpServlet {
    @Autowired
    private CatalogService catalogService;

    @Autowired
    private LogService logService;

    private static final String PRODUCT_FORM = "/WEB-INF/jsp/catalog/product.jsp";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productId = req.getParameter("productId");

        Product product = catalogService.getProduct(productId);
        List<Item> itemList = catalogService.getItemListByProduct(productId);

        HttpSession session = req.getSession();
        session.setAttribute("itemList", itemList);
        session.setAttribute("product", product);

        Account account = (Account) session.getAttribute("account");
        if(account != null){
            logService.logClickButton(session.getId(), account.getUsername(), "Product", productId);
        } else {
            logService.logClickButton(session.getId(), "Product", productId);
        }

        req.getRequestDispatcher(PRODUCT_FORM).forward(req, resp);
    }
}