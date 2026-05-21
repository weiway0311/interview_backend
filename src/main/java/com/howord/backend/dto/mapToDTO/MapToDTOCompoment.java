package com.howord.backend.dto.mapToDTO;

import org.springframework.stereotype.Component;

import com.howord.backend.dto.UserDetailDTO;
import com.howord.backend.user.UserDetail;

@Component
public class MapToDTOCompoment {
	
	public UserDetailDTO mapToUserDetailDTO(UserDetail userDetail) {
		UserDetailDTO dto = new UserDetailDTO();
		dto.setBirth(userDetail.getBirth());
		dto.setEmail(userDetail.getEmail());
		dto.setFullName(userDetail.getFullName());
		dto.setGender(userDetail.getGender());
		dto.setNickName(userDetail.getNickName());
		dto.setPhone(userDetail.getPhone());
		dto.setPhoto(userDetail.getPhoto());
		dto.setRegisterDate(userDetail.getRegisterDate());
		dto.setUserId(userDetail.getUserId());
		dto.setIsEnabled(userDetail.getIsEnabled());
		dto.setAuthority(userDetail.getAuthority());
		return dto;
	}
	
}
