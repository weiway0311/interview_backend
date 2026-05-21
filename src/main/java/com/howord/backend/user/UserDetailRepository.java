package com.howord.backend.user;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface UserDetailRepository extends JpaRepository<UserDetail, String> {
	
	public Optional<UserDetail> findByUserId(String userId);

	public Optional<UserDetail> findByEmail(String username);
	
	@Query(value = """ 
			select count(*) from user_detail 
			""", nativeQuery = true)
	public Long countAll();
	
	public boolean existsByEmail(String email);
	
	@Query("""
		    SELECT DISTINCT u FROM UserDetail u
		    WHERE u.authority != 'ROLE_ADMIN'
			""")
	public Page<UserDetail> findAll(Pageable pageable);
	
}
