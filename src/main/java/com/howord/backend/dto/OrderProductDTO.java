package com.howord.backend.dto;

import java.math.BigDecimal;

public class OrderProductDTO {
	
	private String orderItemId;
	
	private ProductDTO product;
	
    private Integer qty;
    
    private BigDecimal price;
    
    private BigDecimal totalPrice;

	public ProductDTO getProduct() {
		return product;
	}

	public void setProduct(ProductDTO product) {
		this.product = product;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}
    
}
