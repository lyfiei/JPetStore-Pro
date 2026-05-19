package com.csu.jpetstore.mapper;

import com.csu.jpetstore.domain.CartItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartMapper {

    List<CartItem> getCartItemsByUserId(@Param("userId") String userId);

    CartItem getCartItem(@Param("userId") String userId, @Param("itemId") String itemId);

    void insertCartItem(CartItem cartItem, @Param("userId") String userId);

    void updateQuantity(@Param("userId") String userId, @Param("itemId") String itemId, @Param("quantity") int quantity);

    void deleteCartItem(@Param("userId") String userId, @Param("itemId") String itemId);

    void clearCart(@Param("userId") String userId);
}
