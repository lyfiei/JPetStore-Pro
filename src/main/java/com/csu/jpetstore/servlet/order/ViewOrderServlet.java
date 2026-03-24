package com.csu.jpetstore.servlet.order;

import com.csu.jpetstore.domain.Order;
import com.csu.jpetstore.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

public class ViewOrderServlet extends HttpServlet {

    private static final String VIEW_ORDER = "/WEB-INF/jsp/order/viewOrder.jsp";
    private OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int orderId = Integer.parseInt(req.getParameter("orderId"));
        Order order = orderService.getOrder(orderId);

        req.getSession().setAttribute("order", order);
        req.getRequestDispatcher(VIEW_ORDER).forward(req, resp);
    }
}
