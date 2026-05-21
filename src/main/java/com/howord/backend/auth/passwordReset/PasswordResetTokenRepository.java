package com.howord.backend.auth.passwordReset;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.howord.backend.user.UserDetail;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

	// 根據 token 找對應的 PasswordResetToken
    Optional<PasswordResetToken> findByToken(String token);
    
    // 可根據 userDetail 找 Token（如果需要）
    Optional<PasswordResetToken> findByUserDetail(UserDetail userDetail);
	
}
