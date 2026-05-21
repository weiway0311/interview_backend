package com.howord.backend.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.howord.backend.user.UserDetail;
import com.howord.backend.user.UserDetailService;

@Service
public class AuthUserDetailService implements UserDetailsService {
	
	@Autowired
	private UserDetailService userDetailService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetail userDetail = userDetailService.findByEmail(username);
//		List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(userDetail.getAuthority()));
//		return new User(userDetail.getEmail(), userDetail.getPassword(), authorities);
		return new UserDetailsImpl(userDetail);
	}
	
}
