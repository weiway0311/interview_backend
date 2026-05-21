package com.howord.backend.cart;

import java.math.BigDecimal;

import com.howord.backend.product.Product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cart_item")
public class CartItem {
	
	@Id
    @Column(name = "cart_item_id", length = 50)
    private String cartItemId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;
    
    @Column(name = "product_id", length = 50)
    private String productId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    private Product product;
    
    @Column(name = "qty")
    private Integer qty;
    
    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;
    
    @Column(name = "total_price", precision = 10, scale = 2)
    private BigDecimal totalPrice;

	public String getCartItemId() {
		return cartItemId;
	}

	public void setCartItemId(String cartItemId) {
		this.cartItemId = cartItemId;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
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
    
    
    
}
