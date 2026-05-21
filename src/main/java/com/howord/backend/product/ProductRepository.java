package com.howord.backend.product;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

public interface ProductRepository extends JpaRepository<Product, String> {
	
	@Query(""" 
		SELECT p FROM Product p 
		WHERE (:category IS NULL OR p.category = :category) 
		and (:searchTitle is null or p.title like CONCAT('%', :searchTitle, '%')) 
		""")
	public Page<Product> findAll(Pageable pageable,@Param("category")  String category,@Param("searchTitle") String searchTitle);
	
	public Optional<Product> findById(String id);
	
	// 悲觀鎖查詢，用於下單時鎖住該商品行
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT p FROM Product p WHERE p.id = :id")
	Optional<Product> findByIdForUpdate(@Param("id") String id);
	
}
