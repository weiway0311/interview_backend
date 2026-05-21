package com.howord.backend.auth;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.howord.backend.user.UserDetail;

public class UserDetailsImpl implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
	private final UserDetail user;
	
	
	public UserDetailsImpl(UserDetail user) {
        this.user = user;  // 必須初始化 final 欄位
    }
	

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(user.getAuthority()));
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();  // 這仍然是帳號用 email
	}
	
	@Override
    public boolean isEnabled() {
        return user.getIsEnabled() == 1;
    }
	
	public String getId() {
        return user.getUserId();  // 可以取得 UUID 主鍵
    }

    public String getEmail() {
        return user.getEmail();
    }

}
