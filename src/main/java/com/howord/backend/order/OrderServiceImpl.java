package com.howord.backend.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.howord.backend.cart.Cart;
import com.howord.backend.cart.CartItem;
import com.howord.backend.cart.CartItemRepository;
import com.howord.backend.cart.CartRepository;
import com.howord.backend.dto.CheckoutDTO;
import com.howord.backend.product.Product;
import com.howord.backend.product.ProductRepository;
import com.howord.backend.security.IdGenerator;
import com.howord.backend.user.UserDetail;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
	
	@Autowired
    private OrderRepository orderRepository;
	
	@Autowired
	private IdGenerator idGenerator;
	
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Value("${app.domain.backend}")
	private String backendDomain;

	@Value("${app.domain.frontend}")
	private String frontendDomain;
	
	@Override
	public Order checkoutCart(UserDetail user, CheckoutDTO dto) {
	    Cart cart = cartRepository.findByUserDetail(user)
	            .orElseThrow(() -> new RuntimeException("購物車不存在"));

	    if (cart.getItems().isEmpty()) {
	        throw new RuntimeException("購物車是空的");
	    }

	    // 建立訂單
	    Order order = createOrder(user, dto, cart.getItems());

	    // 清空購物車
	    cartItemRepository.deleteAll(cart.getItems());
	    cart.setFinalTotal(BigDecimal.ZERO);
	    cart.getItems().clear();
	    cartRepository.save(cart);

	    return order;
	}
	
	@Override
    public Order createOrder(UserDetail user, CheckoutDTO dto, List<CartItem> cartItems) {
        Order order = new Order();
        order.setOrderId(idGenerator.generateOrderId());
        order.setUserDetail(user);
        order.setRecipientName(dto.getRecipientName());
        order.setRecipientPhone(dto.getRecipientPhone());
        order.setRecipientEmail(dto.getRecipientEmail());
        order.setRecipientAddress(dto.getRecipientAddress());
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus("PENDING");

        BigDecimal total = BigDecimal.ZERO;
        List<OrderProduct> orderProducts = new ArrayList<>();

        for (CartItem item : cartItems) {
//        	Optional<Product> optionalProduct = productRepository.findByIdForUpdate(item.getProductId());
//
//        	if (optionalProduct.isPresent()) {
//        	    Product product = optionalProduct.get();
//        	    // 使用 product 做後續操作
//        	} else {
//        	    throw new RuntimeException("找不到商品：" + item.getProductId());
//        	}
        	Product product = productRepository.findByIdForUpdate(item.getProductId())
        			.orElseThrow(() -> new RuntimeException("找不到商品"));
            // 檢查庫存夠不夠
            if (product.getNum() < item.getQty()) {
                throw new RuntimeException("商品 [" + product.getTitle() + "] 庫存不足");
            }
            // 扣減庫存
            product.setNum(product.getNum() - item.getQty());
        	
            OrderProduct op = new OrderProduct();
            op.setOrderProductId(idGenerator.generate());
            op.setOrder(order);
            op.setProductId(item.getProductId());
            op.setProduct(item.getProduct()); // 假設有 fetch join
            op.setQty(item.getQty());
            op.setPrice(item.getPrice());
            op.setTotalPrice(item.getTotalPrice());

            total = total.add(op.getTotalPrice());
            orderProducts.add(op);
        }

        order.setTotalPrice(total);
        order.setOrderProducts(orderProducts);

        return orderRepository.save(order); // 會自動連同 orderProducts 一起存
    }

    @Override
    public List<Order> getOrdersByUser(UserDetail user) {
        return orderRepository.findByUserDetail(user);
    }

    @Override
    public Optional<Order> getOrderById(String orderId) {
        return orderRepository.findById(orderId);
    }
    
    @Override
    public void markAsPaid(String orderId) {
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            order.setStatus("PAID");
            orderRepository.save(order);
        }
    }
    
    @Override
    public Page<Order> getAllOrders(Pageable pageable){
    	return orderRepository.findAll(pageable);
    }
    
    @Override
    public void updateOrderStatus(String status, String orderId) {
    	orderRepository.updateOrderStatus(status, orderId);
    } 
	
}
