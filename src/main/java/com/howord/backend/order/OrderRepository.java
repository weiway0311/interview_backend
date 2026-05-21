package com.howord.backend.order;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.howord.backend.user.UserDetail;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
	
	// 根據使用者查訂單
    public List<Order> findByUserDetail(UserDetail userDetail);

    // 根據使用者 + 狀態查訂單（例：未付款）
    public List<Order> findByUserDetailAndStatus(UserDetail userDetail, String status);
	
    public Page<Order> findAll(Pageable pageable);
    
    @Query(value = "SELECT COUNT(*) FROM orders o WHERE DATE(o.created_at) = :date", nativeQuery = true)
    public Long countByOrderDate(@Param("date") LocalDate date);
    
    @Modifying
    @Query(value = "UPDATE orders SET status=:status WHERE order_id=:orderId", nativeQuery = true)
    public void updateOrderStatus(@Param("status") String status, @Param("orderId") String orderId);
	
}
