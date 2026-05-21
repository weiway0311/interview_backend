package com.howord.backend.order;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, String> {
	
	// 根據訂單查明細
    List<OrderProduct> findByOrder(Order order);

    // 根據產品 ID 查有哪些訂單含有此產品（可選）
    List<OrderProduct> findByProductId(String productId);
	
}
