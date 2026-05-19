package com.csu.jpetstore.mapper;

import com.csu.jpetstore.domain.LineItem;
import com.csu.jpetstore.domain.Order;
import com.csu.jpetstore.domain.Sequence;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    // Order 相关
    List<Order> getOrdersByUsername(String username);
    Order getOrder(int orderId);
    void insertOrder(Order order);
    void insertOrderStatus(Order order);

    // LineItem 相关
    List<LineItem> getLineItemsByOrderId(int orderId);
    void insertLineItem(LineItem lineItem);

    // Sequence & Inventory 相关
    int getSequence(String name);
    void updateSequence(Sequence sequence);
    void updateInventoryQuantity(Map<String, Object> param);
    int getInventoryQuantity(String itemId);
}