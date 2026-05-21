package com.howord.backend.ecpay;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class EcpayUtils {
	
	public static String generateCheckMacValue(Map<String, String> params, String hashKey, String hashIV) {
		// (1) 排序 key（A-Z 順序）
	    List<String> sortedKeys = new ArrayList<>(params.keySet());
	    Collections.sort(sortedKeys);

	    // (2) 加上 HashKey 開頭與 HashIV 結尾
	    StringBuilder sb = new StringBuilder("HashKey=").append(hashKey);
	    for (String key : sortedKeys) {
	        sb.append("&").append(key).append("=").append(params.get(key));
	    }
	    sb.append("&HashIV=").append(hashIV);

        try {
        	String raw = sb.toString();
//            System.out.println("Raw string: " + raw);
        	
            // (3) URL encode，並符合綠界規定的字元轉換
            String encoded = URLEncoder.encode(raw, StandardCharsets.UTF_8.name())
                .replaceAll("\\%21", "!")
                .replaceAll("\\%28", "(")
                .replaceAll("\\%29", ")")
                .replaceAll("\\%2A", "*")
                .replaceAll("\\%20", "+")
                .replaceAll("\\%2D", "-")
                .replaceAll("\\%2E", ".")
                .replaceAll("\\%5F", "_");
            
            // (4) 全部轉小寫
            String lowerEncoded = encoded.toLowerCase();
//            System.out.println("Lowercase encoded string: " + lowerEncoded);
            
            // (5) SHA-256 加密
            String hash = encrypt(lowerEncoded);
            
            // (6) 轉大寫回傳
            String checkMacValue = hash.toUpperCase();
//            System.out.println("CheckMacValue: " + checkMacValue);

            return checkMacValue;
        } catch (Exception e) {
            throw new RuntimeException("CheckMacValue generation failed", e);
        }
    }
	
	// SHA-256 加密工具方法
    private static String encrypt(String input) throws Exception {
    	MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = md.digest(input.getBytes(StandardCharsets.UTF_8));

        StringBuilder result = new StringBuilder();
        for (byte b : hashBytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
	
}
