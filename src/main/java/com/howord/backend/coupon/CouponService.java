package com.howord.backend.coupon;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.howord.backend.security.IdGenerator;

@Service
@Transactional
public class CouponService {
	
	@Autowired
	CouponRepository couponRepository;
	
	@Autowired
	IdGenerator idGenerator;
	
	public Page<Coupon> getAllCoupons(Pageable pageable) {
		return couponRepository.findAll(pageable);
	}
	
	public Coupon insertCoupon(Coupon coupon) {
		
		if (coupon.getId() != null && !coupon.getId().isBlank()) {
            if (couponRepository.existsById(coupon.getId())) {
                 throw new IllegalArgumentException("嘗試新增時，傳入的 ID '" + coupon.getId() + "' 已存在。請改用更新介面。");
            }
            throw new IllegalArgumentException("嘗試新增時，傳入的 ID '" + coupon.getId() + "' 已存在。請改用更新介面。");
        }

        // 生成新 ID
        String uuid64 = idGenerator.generate();
        coupon.setId(uuid64);
		
		return couponRepository.save(coupon);
	}
	
	// 更新
    public void updateCoupon(Coupon coupon) {
    	if (coupon.getId() == null || coupon.getId().isBlank()) {
            throw new IllegalArgumentException("更新產品必須提供有效的 ID。");
        }
        if (!couponRepository.existsById(coupon.getId())) {
            throw new IllegalArgumentException("該 ID 不存在，無法更新");
        }
        couponRepository.save(coupon); // JPA update
    }
    
    public void deleteCoupon(String id) {
    	couponRepository.deleteById(id);
	}
	
}
