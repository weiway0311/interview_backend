package com.howord.backend.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.howord.backend.dto.CheckoutDTO;
import com.howord.backend.dto.OrderDTO;
import com.howord.backend.dto.OrderProductDTO;
import com.howord.backend.dto.PaginationDTO;
import com.howord.backend.dto.ProductDTO;
import com.howord.backend.dto.mapToDTO.MapToDTOCompoment;
import com.howord.backend.user.UserDetail;
import com.howord.backend.user.UserDetailService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	public UserDetailService userDetailService;
	
	@Autowired
	public OrderService orderService;
	
	@Autowired
	public MapToDTOCompoment mapToDTO;
	
	@PostMapping("/orders")
	public ResponseEntity<?> checkout(
	    @AuthenticationPrincipal UserDetails userDetails,
	    @RequestBody @Valid CheckoutDTO checkoutDTO
	) {
		try {
			
			UserDetail user = userDetailService.findByEmail(userDetails.getUsername());
//			System.out.println(user);
//			System.out.println(checkoutDTO);
			
	    	Order order = orderService.checkoutCart(user, checkoutDTO);
	    	OrderDTO dto = toOrderDTO(order);
	    	
	    	Map<String, Object> response = new HashMap<>();
	    	response.put("success", true);
	        response.put("order", dto);
	    	return ResponseEntity.ok(response);
		}catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "取得prod錯誤");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
		}
	}
	
	private OrderDTO toOrderDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setOrderId(order.getOrderId());
        dto.setRecipientName(order.getRecipientName());
        dto.setRecipientPhone(order.getRecipientPhone());
        dto.setRecipientEmail(order.getRecipientEmail());
        dto.setRecipientAddress(order.getRecipientAddress());
        dto.setTotalPrice(order.getTotalPrice());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUserDetailDTO(mapToDTO.mapToUserDetailDTO(order.getUserDetail()));

        List<OrderProductDTO> items = order.getOrderProducts().stream().map(op -> {
            OrderProductDTO pod = new OrderProductDTO();
            pod.setQty(op.getQty());
            pod.setPrice(op.getPrice());
            pod.setTotalPrice(op.getTotalPrice());
            pod.setOrderItemId(op.getOrderProductId());

            if (op.getProduct() != null) {
                ProductDTO pdto = new ProductDTO();
                pdto.setId(op.getProduct().getId());
                pdto.setTitle(op.getProduct().getTitle());
                pdto.setImageUrl(op.getProduct().getImageUrl());
                pdto.setPrice(op.getProduct().getPrice());
                pod.setProduct(pdto);
            }

            return pod;
        }).collect(Collectors.toList());

        dto.setItems(items);

        return dto;
    }
	
	@GetMapping("/userorders")
    public ResponseEntity<?> getOrders(
            @AuthenticationPrincipal UserDetails userDetails) {
		try {
			
			UserDetail user = userDetailService.findByEmail(userDetails.getUsername());
			
			List<Order> orders = orderService.getOrdersByUser(user);
			
			List<OrderDTO> dtoList = orders.stream()
					.map(this::toOrderDTO)
					.collect(Collectors.toList());
			
			Map<String, Object> response = new HashMap<>();
	    	response.put("success", true);
	        response.put("orderlist", dtoList);
	    	return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "取得prod錯誤");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
		}
    }
	
	@GetMapping("/{orderId}")
    public ResponseEntity<?> getOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String orderId) {
		try {
			
			UserDetail user = userDetailService.findByEmail(userDetails.getUsername());
			
			Optional<Order> orderOpt = orderService.getOrderById(orderId);
			
			if (orderOpt.isEmpty()) {
				return ResponseEntity.notFound().build();
			}
			
			Order order = orderOpt.get();
			
			if (!order.getUserDetail().getUserId().equals(user.getUserId())) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
			
			OrderDTO dto = toOrderDTO(order);
			
			Map<String, Object> response = new HashMap<>();
	    	response.put("success", true);
	        response.put("order", dto);
	    	return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "取得prod錯誤");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
		}
    }
	
	@GetMapping("/all")
	public ResponseEntity<?> getAllOrders(
			@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size ) {
		try {
			Sort sort = Sort.by(Sort.Direction.ASC, "userDetail.email");
			Pageable pageable = PageRequest.of(page - 1, size, sort); // PageRequest 是 0-based
			
			Page<Order> pagedResult = orderService.getAllOrders(pageable);
			

			List<OrderDTO> dto = pagedResult.getContent().stream()
		            .map(order -> this.toOrderDTO(order))
		            .toList();
			
			PaginationDTO paginationDTO = new PaginationDTO();
	        paginationDTO.setTotal_pages(pagedResult.getTotalPages());
	        paginationDTO.setCurrent_page(page);
	        paginationDTO.setHas_pre(pagedResult.hasPrevious());
	        paginationDTO.setHas_next(pagedResult.hasNext());
	        paginationDTO.setCategory("");
	        
	        Map<String, Object> response = new HashMap<>();
	        response.put("success", true);
	        response.put("orders", dto);
	        response.put("pagination", paginationDTO);
	        response.put("messages", "");
			
	        return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "取得prod錯誤");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
		}
	}
	
	@PutMapping("/orderStatus")
	public ResponseEntity<?> updateOrderStatus(@RequestBody OrderDTO dto) {
		try {
//			System.out.println(dto.getStatus());
//			System.out.println(dto.getOrderId());
			orderService.updateOrderStatus(dto.getStatus(), dto.getOrderId());
			
			Map<String, Object> response = new HashMap<>();
	        response.put("success", true);
	        response.put("message", "更新成功");
			
	        return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "更新錯誤");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
		}
	}
	
}
