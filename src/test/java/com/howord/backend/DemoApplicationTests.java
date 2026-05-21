package com.howord.backend;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.howord.backend.order.OrderRepository;

@SpringBootTest
class DemoApplicationTests {
	
	@Autowired
	private OrderRepository orderRepository;
	
	
	
	@Test
	void generateOrderId() {
		try {
			
			String timestamp = new SimpleDateFormat("yyMMdd").format(new Date());
			// 取得今天日期 (LocalDate)
			LocalDate today = LocalDate.now();
			
			// 查詢今天訂單數量
			long orderCountToday = orderRepository.countByOrderDate(today);
			
			// 當天流水號，從1開始，補0到3碼長度
			String orderLengthByDay = String.format("%03d", orderCountToday + 1);
			
			String orderId = "order"+timestamp+orderLengthByDay;
			
			System.out.println(orderId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
