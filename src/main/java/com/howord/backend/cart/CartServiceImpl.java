package com.howord.backend.cart;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.howord.backend.product.Product;
import com.howord.backend.product.ProductRepository;
import com.howord.backend.security.IdGenerator;
import com.howord.backend.user.UserDetail;

@Service
@Transactional
public class CartServiceImpl implements CartService {
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
    private CartItemRepository cartItemRepository;
	
	@Autowired
    private ProductRepository productRepository;
	
	@Autowired
	private IdGenerator idGenerator;

	@Override
	public Cart getOrCreateCart(UserDetail userDetail) {
		return cartRepository.findByUserDetail(userDetail)
				.orElseGet(() -> {
	                Cart cart = new Cart();
	                cart.setCartId(idGenerator.generate());
	                cart.setUserDetail(userDetail);
	                cart.setFinalTotal(BigDecimal.ZERO);
	                return cartRepository.save(cart);
	            });
	}

	@Override
	public void addItem(UserDetail user, String productId, int qty) {
		if (qty <= 0) {
	        throw new IllegalArgumentException("商品數量必須大於 0");
	    }
		
		Cart cart = getOrCreateCart(user);
		Product product = productRepository.findById(productId)
	            .orElseThrow(() -> new RuntimeException("商品不存在"));
		
		BigDecimal price = product.getPrice();
	    List<CartItem> items = cart.getItems();
	    
	 // 檢查是否已有相同商品
	    CartItem existingItem = items.stream()
	            .filter(i -> i.getProductId().equals(productId))
	            .findFirst()
	            .orElse(null);
	    
	    if (existingItem != null) {
	        // 累加數量與小計
	    	int newQty = existingItem.getQty() + qty;
	    	if (product.getNum() >= newQty ) {				
	    		existingItem.setQty(newQty);
	    		existingItem.setTotalPrice(price.multiply(BigDecimal.valueOf(newQty)));
			}else {
				throw new IllegalArgumentException("超過商品庫存數量");
			}
	    } else {
	        // 新增新的項目
	        CartItem item = new CartItem();
	        item.setCartItemId(idGenerator.generate());
	        item.setCart(cart);
	        item.setProductId(productId);
	        item.setQty(qty);
	        item.setPrice(price);
	        item.setTotalPrice(price.multiply(BigDecimal.valueOf(qty)));
	        items.add(item);
	    }
	    
	 // 更新購物車總價
	    recalculateCartTotal(cart);
	    cartRepository.save(cart);
	}

	@Override
	public void updateItemQuantity(String cartItemId, int qty) {
		CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("項目不存在"));

        BigDecimal newTotal = item.getPrice().multiply(BigDecimal.valueOf(qty));
        item.setQty(qty);
        item.setTotalPrice(newTotal);
        cartItemRepository.save(item);

        recalculateCartTotal(item.getCart());
	}

	@Override
	public void removeItem(String cartItemId) {
		CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("項目不存在"));

        Cart cart = item.getCart();
        cart.getItems().remove(item); // 避免記憶體同步問題
        cartItemRepository.delete(item);

        recalculateCartTotal(cart);
        cartRepository.save(cart);
	}

	@Override
	public void clearCart(UserDetail user) {
		Cart cart = cartRepository.findByUserDetail(user)
                .orElseThrow(() -> new RuntimeException("購物車不存在"));

//        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();
        cart.setFinalTotal(BigDecimal.ZERO);
        cartRepository.save(cart);
	}
	
	private void recalculateCartTotal(Cart cart) {
        BigDecimal total = cart.getItems().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setFinalTotal(total);
    }

}
