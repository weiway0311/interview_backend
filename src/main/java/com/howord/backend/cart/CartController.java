package com.howord.backend.cart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.howord.backend.auth.UserDetailsImpl;
import com.howord.backend.dto.CartDTO;
import com.howord.backend.dto.CartItemDTO;
import com.howord.backend.dto.ProductDTO;
import com.howord.backend.user.UserDetail;
import com.howord.backend.user.UserDetailService;

@RestController
@RequestMapping("/cart")
public class CartController {
	
	@Autowired
    private CartService cartService;
	
	@Autowired
	UserDetailService userDetailService;
	
	@GetMapping("/items")
    public ResponseEntity<?> viewCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		try {
	        UserDetail user = userDetailService.findByEmail(userDetails.getEmail());
	        Cart cart = cartService.getOrCreateCart(user);
	        CartDTO dto = cartToDto(cart);
	        
	        Map<String, Object> response = new HashMap<>();
	        response.put("success", true);
	        response.put("message", "");
	        response.put("cart", dto);
	        return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "購物車顯示錯誤");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
		}
    }
	
	public CartDTO cartToDto(Cart cart) {
	    CartDTO dto = new CartDTO();
	    dto.setCartId(cart.getCartId());
	    dto.setFinalTotal(cart.getFinalTotal());

	    List<CartItemDTO> itemDtos = cart.getItems().stream().map(item -> {
	        CartItemDTO itemDto = new CartItemDTO();
	        itemDto.setCartItemId(item.getCartItemId());
	        itemDto.setProductId(item.getProductId());
	        itemDto.setQty(item.getQty());
	        itemDto.setPrice(item.getPrice());
	        itemDto.setTotalPrice(item.getTotalPrice());
	        if (item.getProduct() != null) {
	            ProductDTO productDTO = new ProductDTO();
	            productDTO.setId(item.getProduct().getId());
	            productDTO.setTitle(item.getProduct().getTitle());
	            productDTO.setImageUrl(item.getProduct().getImageUrl());
	            productDTO.setPrice(item.getProduct().getPrice());
	            productDTO.setUnit(item.getProduct().getUnit());
	            productDTO.setNum(item.getProduct().getNum());
	            itemDto.setProduct(productDTO);
	        }
	        return itemDto;
	    }).toList();

	    dto.setItems(itemDtos);
	    return dto;
	}
	
	@PostMapping("/item")
    public ResponseEntity<?> addToCart(
    		@RequestParam String productId,
    		@RequestParam int qty,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
		try {
	        UserDetail user = userDetailService.findByEmail(userDetails.getEmail());
	        cartService.addItem(user, productId, qty);
	        
	        Map<String, Object> response = new HashMap<>();
	        response.put("success", true);
	        response.put("message", "加入購物車成功");
	        return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
		}
    }
	
	@PutMapping("/item/{id}")
    public ResponseEntity<?> updateItem(@PathVariable("id") String cartItemId,
                           @RequestParam int qty) {
        try {
        	cartService.updateItemQuantity(cartItemId, qty);
//        	System.out.println(cartItemId);
//        	System.out.println(qty);
        	
        	Map<String, Object> response = new HashMap<>();
	        response.put("success", true);
	        response.put("message", "更新品項成功");
	        return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "更新品項錯誤");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
		}
    }
	
	@DeleteMapping("/item/{id}")
    public ResponseEntity<?> removeItem(@PathVariable("id") String cartItemId) {
        try {
        	cartService.removeItem(cartItemId);
        	
        	Map<String, Object> response = new HashMap<>();
	        response.put("success", true);
	        response.put("message", "移除品項成功");
	        return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "移除品項錯誤");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
		}
    }
//	
//	@DeleteMapping("/clear")
//    public void clearCart(@AuthenticationPrincipal UserDetailsImpl userDetails) {
//        UserDetail user = userDetailService.findByEmail(userDetails.getEmail());
//        cartService.clearCart(user);
//    }
	
}
