package com.howord.backend.product;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.howord.backend.security.IdGenerator;

@Service
@Transactional
public class ProductService {
	

	@Autowired
	private IdGenerator idGenerator;
	
	@Autowired
	private ProductRepository productRepo;
	
	public Page<Product> getAllProds(Pageable pageable, String category, String searchTitle) {
		return productRepo.findAll(pageable, category, searchTitle);
	}
	
	// 新增
    public Product insertProduct(Product product) {
        if (product.getId() != null && !product.getId().isBlank()) {
            if (productRepo.existsById(product.getId())) {
                 throw new IllegalArgumentException("嘗試新增時，傳入的 ID '" + product.getId() + "' 已存在。請改用更新介面。");
            }
            throw new IllegalArgumentException("嘗試新增時，傳入的 ID '" + product.getId() + "' 已存在。請改用更新介面。");
        }

        // 生成新 ID
        String uuid64 = idGenerator.generate();
        product.setId(uuid64);

        return productRepo.save(product); // 執行新增
    }
    
    // 更新
    public void updateProduct(Product product) {
    	if (product.getId() == null || product.getId().isBlank()) {
            throw new IllegalArgumentException("更新產品必須提供有效的 ID。");
        }
        if (!productRepo.existsById(product.getId())) {
            throw new IllegalArgumentException("該 ID 不存在，無法更新");
        }
        productRepo.save(product); // JPA update
    }
	
	public Optional<Product> getProdById(String id){
		return productRepo.findById(id);
	}
	
	public void deleteProduct(String id) {
		productRepo.deleteById(id);
	}
	
}
