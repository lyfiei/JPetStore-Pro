package com.csu.jpetstore.service;

import com.csu.jpetstore.domain.Cart;
import com.csu.jpetstore.domain.CartItem;
import com.csu.jpetstore.domain.Item;
import com.csu.jpetstore.mapper.CartMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private CatalogService catalogService;

    public List<CartItem> getCartItemsByUserId(String userId){
        return cartMapper.getCartItemsByUserId(userId);
    }

    public CartItem getCartItem(String userId, String itemId){
        return cartMapper.getCartItem(userId, itemId);
    }

    @Transactional
    public void addCartItem(Cart cart, String workingItemId, String userId){
        Item item = catalogService.getItem(workingItemId);

        CartItem cartItem = cart.addItem(item, catalogService.isItemInStock(item.getItemId()));

        cartMapper.insertCartItem(cartItem, userId);
    }

    @Transactional
    public void updateQuantity(Cart cart, String userId, String itemId, int quantity){
        cart.setQuantityByItemId(itemId, quantity);

        cartMapper.updateQuantity(userId, itemId, quantity);
    }

    @Transactional
    public void removeCartItem(Cart cart, String userId, String workingItemId){
        cart.removeItemById(workingItemId);

        cartMapper.deleteCartItem(userId, workingItemId);
    }

    @Transactional
    public void clearCart(String userId){
        cartMapper.clearCart(userId);
    }
}
