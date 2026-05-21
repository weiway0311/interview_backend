package com.howord.backend.coupon;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, String> {
	
	
	public Page<Coupon> findAll(Pageable pageable);
	
}
