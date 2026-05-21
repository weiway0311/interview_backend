package com.howord.backend.auth.passwordReset;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.howord.backend.user.UserDetail;
import com.howord.backend.user.UserDetailRepository;

@Service
@Transactional
public class PasswordResetTokenService {
	
	@Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private UserDetailRepository userDetailRepository;

    // 產生並儲存一個新的密碼重設 token，回傳 token 字串
    @Transactional
    public String createPasswordResetToken(String email) {
        // 先找 user
        UserDetail user = userDetailRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("找不到該 Email"));

        // 產生唯一 token
        String token = UUID.randomUUID().toString();

        // 建立 token entity
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUserDetail(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        resetToken.setUsed(false);

        tokenRepository.save(resetToken);

        return token;
    }

    // 根據 token 查找有效且未使用的 token
    public Optional<PasswordResetToken> findValidToken(String token) {
        Optional<PasswordResetToken> opt = tokenRepository.findByToken(token);
        return opt.filter(t -> !t.isUsed() && t.getExpiryDate().isAfter(LocalDateTime.now()));
    }

    // 重設密碼成功後標記 token 已使用
    @Transactional
    public void markTokenAsUsed(PasswordResetToken token) {
        token.setUsed(true);
        tokenRepository.save(token);
    }
    
    public PasswordResetToken findByToken(String token) {
        return tokenRepository.findByToken(token).orElse(null); // 或丟出自訂例外 if preferred
    }
	
}
