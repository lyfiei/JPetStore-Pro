package com.csu.jpetstore.persistence;

import com.csu.jpetstore.domain.Item;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface ItemMapper {
    void updateInventoryQuantity(Map<String, Object> param);
    int getInventoryQuantity(String itemId);
    List<Item> getItemListByProduct(String productId);
    Item getItem(String itemId);
}