package com.howord.backend.cart;

import com.howord.backend.user.UserDetail;

public interface CartService {
	
	Cart getOrCreateCart(UserDetail user);
	
    void addItem(UserDetail user, String productId, int qty);
    
    void updateItemQuantity(String cartItemId, int qty);
    
    void removeItem(String cartItemId);
    
    void clearCart(UserDetail user);
    
}
