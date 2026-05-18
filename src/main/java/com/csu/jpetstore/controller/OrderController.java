package com.csu.jpetstore.controller;

import com.csu.jpetstore.domain.*;
import com.csu.jpetstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession; // 改成这个
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // 初始进入结算流程
    @GetMapping("/newOrderForm")
    public String newOrderForm(HttpSession session, Model model) {
        Account account = (Account) session.getAttribute("account");
        Cart cart = (Cart) session.getAttribute("cart");

        if (account == null) return "redirect:/account/signonForm";

        Order order = new Order();
        order.initOrder(account, cart);
        session.setAttribute("order", order); // 存入 session 供 AJAX 后续步骤使用

        return "order/orderUnified";
    }

    // 处理 AJAX 提交（保存地址 或 最终确认）
    @PostMapping("/orderUnified")
    @ResponseBody
    public String handleOrderAjax(@RequestParam(required = false) String action,
                                  Order orderFromForm,
                                  HttpSession session) {

        Order sessionOrder = (Order) session.getAttribute("order");
        if (sessionOrder == null) return "session_expired";

        if ("saveInfo".equals(action)) {
            // 将表单传来的地址信息同步到 Session 中的订单对象
            updateOrderAddress(sessionOrder, orderFromForm);
            return "success";
        } else if ("confirmSubmit".equals(action)) {
            // 最终提交数据库
            orderService.insertOrder(sessionOrder);
            session.removeAttribute("cart");
            session.removeAttribute("order");
            return "success";
        }
        return "error";
    }

    private void updateOrderAddress(Order target, Order source) {
        target.setBillToFirstName(source.getBillToFirstName());
        target.setBillToLastName(source.getBillToLastName());
        target.setBillAddress1(source.getBillAddress1());
        target.setBillAddress2(source.getBillAddress2());
        target.setBillCity(source.getBillCity());
        target.setBillState(source.getBillState());
        target.setBillZip(source.getBillZip());
        target.setBillCountry(source.getBillCountry());

        target.setShipToFirstName(source.getShipToFirstName());
        target.setShipToLastName(source.getShipToLastName());
        target.setShipAddress1(source.getShipAddress1());
        target.setShipAddress2(source.getShipAddress2());
        target.setShipCity(source.getShipCity());
        target.setShipState(source.getShipState());
        target.setShipZip(source.getShipZip());
        target.setShipCountry(source.getShipCountry());
    }

    @GetMapping("/viewOrder")
    public String viewOrder(@RequestParam int orderId, Model model) {
        Order order = orderService.getOrder(orderId);
        model.addAttribute("order", order);
        return "order/viewOrder";
    }
}