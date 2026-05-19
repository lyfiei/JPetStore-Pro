package com.csu.jpetstore.service;

import com.csu.jpetstore.domain.*;
import com.csu.jpetstore.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Transactional
    public void insertOrder(Order order) {
        // 1. 获取并更新序列号
        int nextId = orderMapper.getSequence("ordernum");
        orderMapper.updateSequence(new Sequence("ordernum", nextId + 1));
        order.setOrderId(nextId);

        // 2. 更新库存
        for (LineItem lineItem : order.getLineItems()) {
            Map<String, Object> param = new HashMap<>();
            param.put("itemId", lineItem.getItemId());
            param.put("increment", lineItem.getQuantity());
            orderMapper.updateInventoryQuantity(param);
        }

        // 3. 插入订单主表和明细
        orderMapper.insertOrder(order);
        orderMapper.insertOrderStatus(order);
        for (LineItem lineItem : order.getLineItems()) {
            lineItem.setOrderId(order.getOrderId());
            orderMapper.insertLineItem(lineItem);
        }
    }

    public Order getOrder(int orderId) {
        Order order = orderMapper.getOrder(orderId);
        order.setLineItems(orderMapper.getLineItemsByOrderId(orderId));
        return order;
    }

    public List<Order> getOrdersByUsername(String username) {
        return orderMapper.getOrdersByUsername(username);
    }
}