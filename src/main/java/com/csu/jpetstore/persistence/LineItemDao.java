// \src\main\java\com\csu\jpetstore/persistence/LineItemDao.java
package com.csu.jpetstore.persistence;

import com.csu.jpetstore.domain.LineItem;

import java.util.List;

public interface LineItemDao {

    List<LineItem> getLineItemsByOrderId(int orderId);

    void insertLineItem(LineItem lineItem);
}
