package com.howord.backend.cart;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {
	
	List<CartItem> findByCart_CartId(String cartId);
	
}
