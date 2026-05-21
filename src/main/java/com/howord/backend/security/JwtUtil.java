package com.howord.backend.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	
	@Value("${jwt.secret}")
	private String secretKey;
	
//	定義jwt token
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder() // 建立jwt token
                .setSubject(userDetails.getUsername()) // 設定 Subject 為 Username
                .claim("role", userDetails.getAuthorities().iterator().next().getAuthority()) // 加入自定義的欄位：這裡放入「角色（role）」，從 userDetails.getAuthorities() 中取第一個權限
                .setIssuedAt(new Date()) // 設定發行時間（iat）
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 設定過期時間，這裡設為現在時間 + 1 小時
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256) // 使用 HS256 演算法和密鑰做簽名，確保 Token 不能被竄改
                .compact(); // 建立完成，回傳一個 JWT 字串
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder() // 建立 JWT 解析器
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes())) // 設定用來驗證簽名的密鑰
                .build() // 建立解析器實例
                .parseClaimsJws(token) // 驗證並解析 JWT（會自動驗證簽名）
                .getBody() // 取得 payload (claims)
                .getSubject(); // 取得其中的 "sub" 欄位（即 username）
    }

//    public boolean validateToken(String token, UserDetails userDetails) {
//        final String username = extractUsername(token);
//        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
//    }
//
//    private boolean isTokenExpired(String token) {
//        Date expiration = Jwts.parserBuilder()
//                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getExpiration();
//        return expiration.before(new Date());
//    }
    
//    以下2段用來驗證 token 是否有效、是否過期
    public Claims extractClaims(String token) throws JwtException {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token) {
        try {
            Claims claims = extractClaims(token);
            Date expiration = claims.getExpiration();
            return expiration.after(new Date());
        } catch (JwtException e) {
            return false; // 包括過期、簽名錯誤等
        }
    }
}
