package com.howord.backend.coupon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.howord.backend.dto.CouponDTO;
import com.howord.backend.dto.PaginationDTO;

@RestController
@RequestMapping("/coupon")
public class CouponController {
	
	@Autowired
	CouponService couponService;
	
	@GetMapping("/all")
	public ResponseEntity<?> getAllCoupons(
			@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size ) {
		try {
			Sort sort = Sort.by(Sort.Direction.ASC, "title");
			Pageable pageable = PageRequest.of(page - 1, size, sort); // PageRequest 是 0-based
			
			Page<Coupon> pagedResult = couponService.getAllCoupons(pageable);
			

			List<CouponDTO> CouponDTOs = pagedResult.getContent().stream()
		            .map(coupon -> this.mapToDto(coupon))
		            .toList();
			
			PaginationDTO paginationDTO = new PaginationDTO();
	        paginationDTO.setTotal_pages(pagedResult.getTotalPages());
	        paginationDTO.setCurrent_page(page);
	        paginationDTO.setHas_pre(pagedResult.hasPrevious());
	        paginationDTO.setHas_next(pagedResult.hasNext());
	        paginationDTO.setCategory("");
	        
	        Map<String, Object> response = new HashMap<>();
	        response.put("success", true);
	        response.put("coupons", CouponDTOs);
	        response.put("pagination", paginationDTO);
	        response.put("message", "");
			
	        return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "取得prod錯誤");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
		}
	}
	
	private CouponDTO mapToDto(Coupon coupon) {
		if (coupon == null) {
			return null;
		}
		CouponDTO dto = new CouponDTO();
		dto.setId(coupon.getId());
		dto.setCode(coupon.getCode());
		dto.setDueDate(coupon.getDueDate());
		dto.setIsEnabled(coupon.getIsEnabled());
		dto.setPercent(coupon.getPercent());
		dto.setTitle(coupon.getTitle());
		return dto;
	}
	private Coupon mapToEntity(CouponDTO dto) {
		if (dto == null) {
			return null;
		}
		Coupon coupon = new Coupon();
		coupon.setId(dto.getId());
		coupon.setCode(dto.getCode());
		coupon.setDueDate(dto.getDueDate());
		coupon.setIsEnabled(dto.getIsEnabled());
		coupon.setPercent(dto.getPercent());
		coupon.setTitle(dto.getTitle());
		return coupon;
	}
	
	@PostMapping("/coupon")
    public ResponseEntity<?> insertProduct(@RequestBody CouponDTO dto) {
		try {
			Coupon coupon = mapToEntity(dto);
			couponService.insertCoupon(coupon);
			
			Map<String, Object> response = new HashMap<>();
	        response.put("success", true);
	        response.put("message", "新增成功");
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "新增失敗");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
    }
	
	@PutMapping("/coupon/{id}")
	public ResponseEntity<?> updateProduct(@PathVariable String id, @RequestBody CouponDTO dto) {
		try {
			Coupon coupon = mapToEntity(dto);
			couponService.updateCoupon(coupon);
			
			Map<String, Object> response = new HashMap<>();
	        response.put("success", true);
	        response.put("message", "更新成功");
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "新增失敗");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}
	
	@DeleteMapping("/coupon/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable String id) {
		try {
			couponService.deleteCoupon(id);
			
			Map<String, Object> response = new HashMap<>();
	        response.put("success", true);
	        response.put("message", "刪除成功");
			
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "刪除失敗");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}
	
}
