package com.howord.backend.cart;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.howord.backend.user.UserDetail;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
	
	@EntityGraph(attributePaths = { "items", "items.product" })
	Optional<Cart> findByUserDetail(UserDetail userDetail);
	
}
