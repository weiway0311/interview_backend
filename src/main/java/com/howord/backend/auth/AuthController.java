package com.howord.backend.auth;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.howord.backend.auth.passwordReset.PasswordResetToken;
import com.howord.backend.auth.passwordReset.PasswordResetTokenService;
import com.howord.backend.dto.PasswordResetDTO;
import com.howord.backend.mail.MailService;
import com.howord.backend.security.JwtUtil;
import com.howord.backend.user.UserDetail;
import com.howord.backend.user.UserDetailRepository;
import com.howord.backend.user.UserDetailService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
    private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	
	@Autowired
	private PasswordResetTokenService tokenService;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private UserDetailRepository userDetailRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserDetailService userDetailService;
	

	/**
	 * 驗整登入密碼
	 */
	@PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
		if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
	        return ResponseEntity.badRequest().body("帳號或密碼不可為空");
	    }

        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(), 
                    loginRequest.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtil.generateToken(userDetails);

            // 回傳 JWT 給前端
            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("success", "true");
            response.put("username", userDetails.getUsername());
            response.put("authority", userDetails.getAuthorities());
            

            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
        	Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "帳號或密碼錯誤");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
	
	/**
	 * 驗整 token 是否存活
	 */
	@PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "缺少 token"));
        }

        String token = authHeader.substring(7); // 拿掉 Bearer 
        boolean valid = jwtUtil.isTokenValid(token);

        if (valid) {
            return ResponseEntity.ok(Map.of("success", true, "message", "Token 有效"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "Token 已過期或無效"));

        }
    }
	
	
	// 1. 忘記密碼，輸入 email，寄送重設連結
    @PostMapping("/password-reset-tokens")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        try {
            String token = tokenService.createPasswordResetToken(email);
            mailService.sendResetPasswordMail(email, token);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", "true");
            response.put("message", "重設連結已寄出，請查收信箱");
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "找不到該 Email");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            
        } catch (Exception e) {
        	Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "系統錯誤");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
    
    // 2. 取得 token email
    @GetMapping("/password-reset-tokens/{token}")
    public ResponseEntity<?> getTokenInfo(@PathVariable String token) {
    	try {
    		PasswordResetToken prt = tokenService.findByToken(token);
    		if (prt == null) {
    			Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Token 無效");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
    		
    		Map<String, Object> response = new HashMap<>();
            response.put("success", "true");
            response.put("email", prt.getUserDetail().getEmail());
            return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "系統錯誤");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
		}
    }

    // 3. 重設密碼，輸入 token 和新密碼
    @PutMapping("/password-reset-tokens/{token}")
    public ResponseEntity<?> resetPassword(
    		@PathVariable String token, 
    		@Valid @RequestBody PasswordResetDTO dto) {
    	try {
    		Optional<PasswordResetToken> optToken = tokenService.findValidToken(token);
            if (optToken.isEmpty()) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Token 不存在或已使用過");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }

            PasswordResetToken resetToken = optToken.get();
            UserDetail user = resetToken.getUserDetail();

            user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
            userDetailRepository.save(user);

            tokenService.markTokenAsUsed(resetToken);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "密碼重設成功");
            return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "系統錯誤");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
		}
    }
    
    @PostMapping("/new_user")
    public ResponseEntity<?> register(@RequestBody RegisterDTO dto ){
    	try {
            UserDetail savedUser = userDetailService.register(dto);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "新增帳號成功，5秒後轉跳");
            response.put("user", savedUser.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
        	Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "系統錯誤");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
    
}
