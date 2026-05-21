package com.howord.backend.dto.mapToDTO;

import org.springframework.stereotype.Component;

import com.howord.backend.dto.UserDetailDTO;
import com.howord.backend.user.UserDetail;

@Component
public class MapToEntityComponent {
	
	public UserDetail mapToUserDetailDTO(UserDetailDTO dto) {
		UserDetail userDetail = new UserDetail();
		userDetail.setBirth(dto.getBirth());
		userDetail.setEmail(dto.getEmail());
		userDetail.setFullName(dto.getFullName());
		userDetail.setGender(dto.getGender());
		userDetail.setNickName(dto.getNickName());
		userDetail.setPhone(dto.getPhone());
		userDetail.setPhoto(dto.getPhoto());
		userDetail.setRegisterDate(dto.getRegisterDate());
		userDetail.setUserId(dto.getUserId());
		userDetail.setIsEnabled(dto.getIsEnabled());
		return userDetail;
	}
	
}
