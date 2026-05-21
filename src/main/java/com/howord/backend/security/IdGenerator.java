package com.howord.backend.security;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.howord.backend.order.OrderRepository;
import com.howord.backend.user.UserDetailRepository;

@Component
public class IdGenerator {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private UserDetailRepository userDetailRepository;
	
	public String generate() {
        UUID uuid = UUID.randomUUID();

        // 將 UUID 拆成兩個 long 並寫入 ByteBuffer
        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());

        // 使用 URL-safe 的 Base64 編碼，並移除 padding（等號）
        return Base64.getUrlEncoder()
                     .withoutPadding()
                     .encodeToString(bb.array());
    }
	
	public String generateOrderId() {
		String timestamp = new SimpleDateFormat("yyMMdd").format(new Date());
		// 取得今天日期 (LocalDate)
	    LocalDate today = LocalDate.now();

	    // 查詢今天訂單數量
	    Long count = orderRepository.countByOrderDate(today);
	    long orderCountToday = count != null ? count : 0;

	    // 當天流水號，從1開始，補0到3碼長度
	    String orderLengthByDay = String.format("%03d", orderCountToday + 1);
	    
		String orderId = "order"+timestamp+orderLengthByDay;
		
		return orderId;
	}
	
	public String generateUserId() {
		Long count = userDetailRepository.countAll();
		long userCount = count != null ? count : 0;
		
		String userLength = String.format("%05d", userCount + 1);
		String userId = "user" + userLength;
		
		return userId;
	}
}
