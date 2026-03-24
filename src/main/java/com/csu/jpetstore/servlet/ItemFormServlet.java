package com.csu.jpetstore.servlet;

import com.csu.jpetstore.domain.Account;
import com.csu.jpetstore.domain.Item;
import com.csu.jpetstore.domain.Product;
import com.csu.jpetstore.service.CatalogService;
import com.csu.jpetstore.service.LogService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ItemFormServlet extends HttpServlet {
    private CatalogService catalogService;

    private static final String ITEM_FORM = "/WEB-INF/jsp/catalog/item.jsp";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        catalogService = new CatalogService();

        String itemId = request.getParameter("itemId");
        Item item = catalogService.getItem(itemId);
        Product product = item.getProduct();

        HttpSession session = request.getSession();
        session.setAttribute("item", item);
        session.setAttribute("product", product);

        LogService logService = new LogService();
        Account account = (Account) session.getAttribute("account");
        if(account != null){
            logService.logClickButton(session.getId(),account.getUsername(),"Item",itemId);
        }
        else{
            logService.logClickButton(session.getId(),"Item",itemId);
        }

        request.getRequestDispatcher(ITEM_FORM).forward(request, response);

    }
}
