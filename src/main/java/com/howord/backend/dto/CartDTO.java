package com.howord.backend.dto;

import java.math.BigDecimal;
import java.util.List;

public class CartDTO {
	
	private String cartId;
	
    private BigDecimal finalTotal;
    
    private List<CartItemDTO> items;

	public String getCartId() {
		return cartId;
	}

	public void setCartId(String cartId) {
		this.cartId = cartId;
	}

	public BigDecimal getFinalTotal() {
		return finalTotal;
	}

	public void setFinalTotal(BigDecimal finalTotal) {
		this.finalTotal = finalTotal;
	}

	public List<CartItemDTO> getItems() {
		return items;
	}

	public void setItems(List<CartItemDTO> items) {
		this.items = items;
	}
    
    

}
