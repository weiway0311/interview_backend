package com.howord.backend.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.howord.backend.auth.UserDetailsImpl;
import com.howord.backend.dto.PaginationDTO;
import com.howord.backend.dto.UserDetailDTO;
import com.howord.backend.dto.mapToDTO.MapToDTOCompoment;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/users")
public class UserDetailController {


	@Autowired
	private UserDetailService userDetailService;
	
	@Autowired
	private MapToDTOCompoment mapToDto;

	
	
	@GetMapping("/userdetail")
	public ResponseEntity<?> getUserDetailById(
			@AuthenticationPrincipal UserDetailsImpl userDetails) {
		try {			
			UserDetail user = userDetailService.findByEmail(userDetails.getUsername());
			UserDetailDTO dto = mapToDto.mapToUserDetailDTO(user);
			
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "更新成功");
			response.put("userDetail", dto);
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "取得userdetail錯誤");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
		}
	}
	
	@PutMapping("/userdetail")
	public ResponseEntity<?> updateSelf(
			@AuthenticationPrincipal UserDetailsImpl userDetails,
			 @Valid @RequestBody UserDetailDTO dto){
		try {			
			userDetailService.updateUserDetail(dto, userDetails.getId());
			
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "更新成功");
//			response.put("userDetail", dto);
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "取得userdetail錯誤");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
		}
	}
	
	@GetMapping("/users")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getUsers(
			@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
		try {
			Pageable pageable = PageRequest.of(page - 1, size);
			Page<UserDetailDTO> pagedResult = userDetailService.getUsers(pageable);
			
			PaginationDTO paginationDTO = new PaginationDTO();
	        paginationDTO.setTotal_pages(pagedResult.getTotalPages());
	        paginationDTO.setCurrent_page(page);
	        paginationDTO.setHas_pre(pagedResult.hasPrevious());
	        paginationDTO.setHas_next(pagedResult.hasNext());
			
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "成功");
			response.put("users", pagedResult.getContent());
			response.put("pagination", paginationDTO);
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "系統錯誤");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
		}
	}
	
	// 管理員修改其他人資料
	@PutMapping("/userdetail/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> updateById(
			@PathVariable String id, 
			@RequestBody UserDetailDTO dto){
		try {
			userDetailService.updateUser(id, dto);
			
			Map<String, Object> response = new HashMap<>();
			response.put("success", true);
			response.put("message", "更新成功");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "系統錯誤");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
		}
	}
	
}
