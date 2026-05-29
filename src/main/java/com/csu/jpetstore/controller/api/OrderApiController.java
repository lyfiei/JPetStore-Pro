package com.csu.jpetstore.controller.api;

import com.csu.jpetstore.common.Result;
import com.csu.jpetstore.domain.*;
import com.csu.jpetstore.service.CartService;
import com.csu.jpetstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderApiController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @GetMapping
    public Result<Map<String, Object>> getOrderList(@RequestParam(defaultValue = "1") int page,
                                                     @RequestParam(defaultValue = "10") int pageSize,
                                                     HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        List<Order> orders = orderService.getOrdersByUsername(username);

        Map<String, Object> data = new HashMap<>();
        data.put("list", orders);
        data.put("total", orders.size());
        data.put("page", page);
        data.put("pageSize", pageSize);
        return Result.success(data);
    }

    @GetMapping("/{orderId}")
    public Result<Order> getOrderDetail(@PathVariable int orderId) {
        return Result.success(orderService.getOrder(orderId));
    }

    @PostMapping
    public Result<Map<String, Object>> createOrder(@RequestBody Map<String, Object> body,
                                                    HttpServletRequest request) {
        String username = (String) request.getAttribute("username");

        // 从数据库加载购物车
        List<CartItem> cartItems = cartService.getCartItemsByUserId(username);
        if (cartItems.isEmpty()) {
            Map<String, Object> err = new HashMap<>();
            err.put("code", 400);
            err.put("message", "购物车为空");
            return Result.error(400, "购物车为空");
        }

        Cart cart = new Cart();
        for (CartItem ci : cartItems) {
            cart.addItem(ci.getItem(), ci.isInStock());
            cart.setQuantityByItemId(ci.getItem().getItemId(), ci.getQuantity());
        }

        // 构建订单
        Order order = new Order();
        order.setUsername(username);
        order.setOrderDate(new Date());
        order.setStatus("P");
        order.setTotalPrice(cart.getSubTotal());

        setIfPresent(body, "billToFirstName", order::setBillToFirstName);
        setIfPresent(body, "billToLastName", order::setBillToLastName);
        setIfPresent(body, "billAddress1", order::setBillAddress1);
        setIfPresent(body, "billAddress2", order::setBillAddress2);
        setIfPresent(body, "billCity", order::setBillCity);
        setIfPresent(body, "billState", order::setBillState);
        setIfPresent(body, "billZip", order::setBillZip);
        setIfPresent(body, "billCountry", order::setBillCountry);
        setIfPresent(body, "shipToFirstName", order::setShipToFirstName);
        setIfPresent(body, "shipToLastName", order::setShipToLastName);
        setIfPresent(body, "shipAddress1", order::setShipAddress1);
        setIfPresent(body, "shipAddress2", order::setShipAddress2);
        setIfPresent(body, "shipCity", order::setShipCity);
        setIfPresent(body, "shipState", order::setShipState);
        setIfPresent(body, "shipZip", order::setShipZip);
        setIfPresent(body, "shipCountry", order::setShipCountry);
        setIfPresent(body, "creditCard", order::setCreditCard);
        setIfPresent(body, "expiryDate", order::setExpiryDate);
        setIfPresent(body, "cardType", order::setCardType);
        setIfPresent(body, "courier", order::setCourier);

        order.setLocale("CN");

        // 添加订单项
        for (CartItem ci : cart.getCartItemList()) {
            order.addLineItem(ci);
        }

        orderService.insertOrder(order);

        // 清空购物车
        cartService.clearCart(username);

        Map<String, Object> data = new HashMap<>();
        data.put("orderId", order.getOrderId());
        return Result.success(data);
    }

    private void setIfPresent(Map<String, Object> body, String key, java.util.function.Consumer<String> setter) {
        Object value = body.get(key);
        if (value != null) {
            setter.accept(value.toString());
        }
    }
}
