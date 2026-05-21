package com.howord.backend.cart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.howord.backend.user.UserDetail;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "cart")
public class Cart  {
	
	@Id @Column(name = "cart_id", length = 50)
    private String cartId;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserDetail userDetail;
	
	@Column(name = "final_total", precision = 10, scale = 2)
    private BigDecimal finalTotal;
	
	@OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> items = new ArrayList<>();

	public String getCartId() {
		return cartId;
	}

	public void setCartId(String cartId) {
		this.cartId = cartId;
	}

	public UserDetail getUserDetail() {
		return userDetail;
	}

	public void setUserDetail(UserDetail userDetail) {
		this.userDetail = userDetail;
	}

	public BigDecimal getFinalTotal() {
		return finalTotal;
	}

	public void setFinalTotal(BigDecimal finalTotal) {
		this.finalTotal = finalTotal;
	}

	public List<CartItem> getItems() {
		return items;
	}

	public void setItems(List<CartItem> items) {
		this.items = items;
	}
	
	
	
}
