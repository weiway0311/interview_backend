package com.howord.backend.security;

import java.io.IOException;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }
    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
//        List<String> excludedPaths = List.of("/auth/login", "/auth/register", 
//        		"/swagger-ui/index.html");
//        return excludedPaths.contains(path);
        
        // 直接用 startsWith 判斷較靈活
        return path.startsWith("/auth/login") ||
               path.startsWith("/auth/register") ||
               path.startsWith("/auth/password-reset-tokens") ||
               path.startsWith("/auth/newuser") ||
               path.startsWith("/product/") ||
               path.startsWith("/payment/ecpaynotify") ||  // ecpay Request
               path.startsWith("/swagger-ui") ||           // UI 靜態頁面
               path.startsWith("/v3/api-docs") ||          // OpenAPI 資訊
               path.startsWith("/swagger-resources") ||    // Swagger 靜態資源
               path.startsWith("/webjars");                // JS/CSS 依賴
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
//        System.out.println("AuthHeader: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.setContentType("application/json;charset=UTF-8");
//            response.getWriter().write(new ObjectMapper().writeValueAsString(
//                    Map.of("success", false, "message", "缺少或格式錯誤的 Authorization token")
//            ));
//            return;
            // 直接拋 AuthenticationException，Spring Security 會捕捉並呼叫 EntryPoint
            throw new InsufficientAuthenticationException("缺少或格式錯誤的 Authorization token");
        }

        String token = authHeader.substring(7);
//        System.out.println("Extracted token: " + token);

        try {
            if (jwtUtil.isTokenValid(token)) {
                String username = jwtUtil.extractUsername(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                // 建立一個 Spring Security 的認證物件（Authentication 介面的實作）
                // 第一個參數：userDetails（身份資訊）
                // 第二個參數：null（因為我們已用 JWT 驗證，不需要密碼）
                // 第三個參數：使用者的權限列表（角色）

                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                // 把這次請求的其他資訊（像是 IP、Session ID 等）塞進這個認證物件的細節中。這對某些場景下記錄登入紀錄、審計日誌等會有幫助。

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                // 核心操作：把剛剛建好的 Authentication 放入 Spring Security 的安全上下文中（SecurityContext）
                // 等於告訴 Spring Security：這個使用者已經通過驗證，可以讓他繼續使用後台系統。
                filterChain.doFilter(request, response);
                // 放行請求，讓它繼續往下一個 Filter 或 Controller 執行。
                // 若不呼叫這一行，請求就會被卡住不會繼續下去。
            } else {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.setContentType("application/json;charset=UTF-8");
//                response.getWriter().write(new ObjectMapper().writeValueAsString(
//                        Map.of("success", false, "message", "Token 已過期或無效")
//                ));
                throw new BadCredentialsException("Token 已過期或無效");
            }
        } catch (Exception e) {
//        	e.printStackTrace();
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.setContentType("application/json;charset=UTF-8");
//            response.getWriter().write(new ObjectMapper().writeValueAsString(
//                    Map.of("success", false, "message", "Token 驗證失敗: " + e.getMessage())
//            ));
            SecurityContextHolder.clearContext();
            throw new AuthenticationServiceException("Token 驗證失敗", e);
        }
    }
}
