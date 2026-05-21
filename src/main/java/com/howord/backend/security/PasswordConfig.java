package com.howord.backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
		
		// 自訂的 PasswordEncoder，強制所有密碼都當明文比對
//		return new PasswordEncoder() {	
//	        @Override
//	        public String encode(CharSequence rawPassword) {
//	            return rawPassword.toString(); // 不編碼，直接原文回傳
//	        }
//
//	        @Override
//	        public boolean matches(CharSequence rawPassword, String encodedPassword) {
//	            return rawPassword.toString().equals(encodedPassword);
//	        }
//	    };
	}
	
}
