package com.howord.backend.user;


import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.howord.backend.auth.RegisterDTO;
import com.howord.backend.dto.UserDetailDTO;
import com.howord.backend.dto.mapToDTO.MapToDTOCompoment;
import com.howord.backend.exception.UserNotFoundException;
import com.howord.backend.security.IdGenerator;


@Service
@Transactional
public class UserDetailService {
	
	@Autowired
	private UserDetailRepository userDetailRepo;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
	private IdGenerator idGenerator;
	
	@Autowired
	private MapToDTOCompoment mapToDTO;
	
	public UserDetail findByEmail(String email) {
		Optional<UserDetail> user = userDetailRepo.findByEmail(email);
		
		if (user.isEmpty()) {
			throw new UserNotFoundException("user not found");
		}
		
		return user.get();
	}
	

	public void updateUserDetail(UserDetailDTO dto, String id) {
		UserDetail user = userDetailRepo.findByUserId(id)
		        .orElseThrow(() -> new RuntimeException("User not found"));
		
		if (dto.getFullName() != null) user.setFullName(dto.getFullName());
	    if (dto.getNickName() != null) user.setNickName(dto.getNickName());
	    if (dto.getGender() != null) user.setGender(dto.getGender());
	    if (dto.getPhoto() != null) user.setPhoto(dto.getPhoto());
	    if (dto.getBirth() != null) user.setBirth(dto.getBirth());
	    if (dto.getPhone() != null) user.setPhone(dto.getPhone());
//	    System.out.println(user);

	    userDetailRepo.save(user); // 只更新有修改的欄位
	}
	
	public UserDetail register(RegisterDTO dto) {
        if (userDetailRepo.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email 已存在");
        }

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new RuntimeException("兩次密碼不一致");
        }

        UserDetail user = new UserDetail();
        user.setUserId(idGenerator.generateUserId());
        user.setEmail(dto.getEmail());
        user.setFullName(dto.getFullName());
        user.setNickName(dto.getNickName());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setBirth(dto.getBirth());
        user.setAuthority("ROLE_CUSTOMER");
        user.setBirth(dto.getBirth());
        user.setGender(dto.getGender());
        user.setPhone(dto.getPhone());
        user.setIsEnabled(1);
        user.setRegisterDate(new Date());

        return userDetailRepo.save(user);
    }
	
	public Page<UserDetailDTO> getUsers(Pageable pageable){
		Page<UserDetail> page = userDetailRepo.findAll(pageable);
		Page<UserDetailDTO> dtoList = page
				.map(user -> mapToDTO.mapToUserDetailDTO(user));
		
		return dtoList;
	}

	@Transactional
	public void updateUser(String id, UserDetailDTO dto) {
		UserDetail user = userDetailRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));	

		if (dto.getIsEnabled() != null) { 
		    user.setIsEnabled(dto.getIsEnabled());
		}
		
		if (dto.getAuthority() != null) {
			user.setAuthority(dto.getAuthority());
		}
//		System.out.println(user);
		userDetailRepo.save(user);
	}

	
}
