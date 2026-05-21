package com.howord.backend.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.howord.backend.auth.AuthUserDetailService;
import com.howord.backend.security.JwtAuthenticationEntryPoint;
import com.howord.backend.security.JwtAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	@Autowired
    private AuthUserDetailService authUserDetailService;
    
	// 登入驗證
	@Bean
    AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception{
		AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder
            .userDetailsService(authUserDetailService)
            .passwordEncoder(passwordEncoder); // 改為使用參數，而不是呼叫自己的方法

        return authBuilder.build();
	}
	
	// 基於 Spring MVC 的web索引增加cors，但spring security會比致段優先，之後要替換掉 
//	@Bean
//    public WebMvcConfigurer corsConfigurer() {	
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/api/**")
//                        .allowedOrigins("http://localhost:5173")  // 你的 React 埠號
//                        .allowedMethods("*")
//                        .allowedHeaders("*")
//                        .allowCredentials(true);
//            }
//        };
//    }
	
	// 專門提供給 Spring Security 用來做 CORS 設定的 Bea
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration config = new CorsConfiguration();
	    config.setAllowedOrigins(List.of("http://localhost:5173"));
	    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
	    config.setAllowedHeaders(List.of("*"));
	    config.setAllowCredentials(true);

	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", config);  // 這裡用 /** 確保涵蓋所有 API 路徑
	    return source;
	}
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter, 
			JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) throws Exception {
	    http.cors(cors -> {})
	        .csrf(csrf -> csrf.disable())
	        .authorizeHttpRequests(auth -> auth
//	        	.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()  // 允許所有 OPTIONS 請求
	            .requestMatchers("/auth/login").permitAll()
	            .requestMatchers("/auth/password-reset-tokens/**").permitAll()
	            .requestMatchers("/auth/validate").permitAll()
	            .requestMatchers("/auth/newuser").permitAll()
	            .requestMatchers("/product/**").permitAll()
	            .requestMatchers("/payment/ecpaynotify").permitAll()
	            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()
	            .anyRequest().authenticated()
	        )
	        .formLogin(form -> form.disable())
	        .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint));
	    
	    http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
	    return http.build();
	}
	
	
    
    
}
