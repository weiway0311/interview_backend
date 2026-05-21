package com.howord.backend.order;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.howord.backend.cart.CartItem;
import com.howord.backend.dto.CheckoutDTO;
import com.howord.backend.user.UserDetail;

public interface OrderService {
	
	public Order checkoutCart(UserDetail user, CheckoutDTO dto);
	
	public Order createOrder(UserDetail user, CheckoutDTO dto, List<CartItem> cartItems);

	public List<Order> getOrdersByUser(UserDetail user);

	public Optional<Order> getOrderById(String orderId);

	public void markAsPaid(String orderId);
	
	public Page<Order> getAllOrders(Pageable pageable);
	
	public void updateOrderStatus(String status, String orderId);
	
}
