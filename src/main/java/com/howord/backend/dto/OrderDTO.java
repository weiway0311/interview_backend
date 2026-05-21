package com.howord.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


public class OrderDTO {

	private String orderId;
	
    private String recipientName;
    
    private String recipientPhone;
    
    private String recipientEmail;
    
    private String recipientAddress;
    
    private BigDecimal totalPrice;
    
    private String status;
    
    private LocalDateTime createdAt;
    
    private List<OrderProductDTO> items;
    
    private UserDetailDTO userDetailDTO;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getRecipientName() {
		return recipientName;
	}

	public void setRecipientName(String recipientName) {
		this.recipientName = recipientName;
	}

	public String getRecipientPhone() {
		return recipientPhone;
	}

	public void setRecipientPhone(String recipientPhone) {
		this.recipientPhone = recipientPhone;
	}

	public String getRecipientEmail() {
		return recipientEmail;
	}

	public void setRecipientEmail(String recipientEmail) {
		this.recipientEmail = recipientEmail;
	}

	public String getRecipientAddress() {
		return recipientAddress;
	}

	public void setRecipientAddress(String recipientAddress) {
		this.recipientAddress = recipientAddress;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public List<OrderProductDTO> getItems() {
		return items;
	}

	public void setItems(List<OrderProductDTO> items) {
		this.items = items;
	}

	public UserDetailDTO getUserDetailDTO() {
		return userDetailDTO;
	}

	public void setUserDetailDTO(UserDetailDTO userDetailDTO) {
		this.userDetailDTO = userDetailDTO;
	}

	
	
}
