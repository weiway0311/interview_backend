package com.howord.backend.ecpay;

import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.howord.backend.dto.OrderDTO;
import com.howord.backend.order.Order;
import com.howord.backend.order.OrderService;

@RestController
@RequestMapping("/payment")
public class EcpayController {
	
	@Autowired
	OrderService orderService;
	
	private String RETURN_URL = "https://8a7f-61-222-220-127.ngrok-free.app/api/payment/ecpaynotify"; // 通知後端
	
	private String FRONTEND_SUCCESS_URL = "http://localhost:5173/#/Success/"; // 付款完成後前端導回
	
//	測試
	private String MERCHANT_ID = "3002607";

	private String HASH_KEY = "pwFHCqoQZGmho4w6";

	private String HASH_IV = "EkRm7iFT261dpevs";
//	| 卡號                   | 到期年月   | 安全碼 |
//	| --------------------- | ------- | --- |
//	| `4311-9522-2222-2222` | 12 / 29 | 222 |

	
	@PostMapping("/ecpayprepare")
	public ResponseEntity<?> prepareEcpay(@RequestBody OrderDTO dto) {
		try {
			Optional<Order> orderOpt = orderService.getOrderById(dto.getOrderId());
			if (orderOpt.isEmpty()) {
				return ResponseEntity.notFound().build();
			}
			Order order = orderOpt.get();
			
			String MerchantTradeNo = createMerchantTradeNo(order);
			
			Map<String, String> params = new HashMap<>();
		    params.put("MerchantID", MERCHANT_ID);
		    params.put("MerchantTradeNo", MerchantTradeNo);
		    params.put("MerchantTradeDate", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
		    params.put("PaymentType", "aio");
		    params.put("TotalAmount", String.valueOf(order.getTotalPrice().setScale(0, RoundingMode.HALF_UP).intValue()));
		    params.put("TradeDesc", "面試專案付款模擬");
		    params.put("ItemName", order.getProductSummary()); // 像是「品項A#品項B」
		    params.put("ReturnURL", RETURN_URL);
		    params.put("ClientBackURL", FRONTEND_SUCCESS_URL + dto.getOrderId());
		    params.put("ChoosePayment", "Credit");
		    params.put("EncryptType", "1");
		    String checkMacValue = EcpayUtils.generateCheckMacValue(params, HASH_KEY, HASH_IV);
		    params.put("CheckMacValue", checkMacValue);
		    
		    return ResponseEntity.ok(params);
		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "取得prepareEcpay錯誤");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
		}
		
	}

	private String createMerchantTradeNo(Order order) {
		String baseOrderId = order.getOrderId();
		String timestamp = String.valueOf(System.currentTimeMillis());
		// 準備要加的後綴字元數（包含中間的'-'）
		int maxSuffixLength = 20 - baseOrderId.length() - 1; // -1 是中間的 '-'
		// 取 timestamp 後面 maxSuffixLength 長度的字串
		if (maxSuffixLength <= 0) {
		    // 如果 baseOrderId 本身就超過或剛好20字，直接用它
		    // 或者你可以切割 baseOrderId
		    baseOrderId = baseOrderId.substring(0, 19); // 預留1給 '-'
		    maxSuffixLength = 1;
		}
		String shortTimestamp = timestamp.substring(timestamp.length() - maxSuffixLength);
		String MerchantTradeNo = baseOrderId+ "Z" +shortTimestamp;
		return MerchantTradeNo;
	}
	
	@PostMapping("/ecpaynotify")
	public ResponseEntity<String> handleEcpayNotify(@RequestParam Map<String, String> form) {
		String orderId = form.get("MerchantTradeNo").split("Z")[0];
	    String rtnCode = form.get("RtnCode");
	    String tradeAmt = form.get("TradeAmt");

	    System.out.println("收到綠界付款通知，訂單號碼: {" + orderId + "}, 狀態: {" + rtnCode + "}, 金額: {" + tradeAmt + "}");

	    if ("1".equals(rtnCode)) {
	        // orderService.markAsPaid(orderId);
	        System.out.println("付款成功更新order");
	    }

	    //  回傳純文字 text/plain 並且狀態碼是 200
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.TEXT_PLAIN);
	    return new ResponseEntity<>("1|OK", headers, HttpStatus.OK);
	}
	
	
}
