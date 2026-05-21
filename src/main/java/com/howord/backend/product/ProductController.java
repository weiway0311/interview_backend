package com.howord.backend.product;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.howord.backend.dto.PaginationDTO;
import com.howord.backend.dto.ProductDTO;


@RestController
@RequestMapping("/product")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@Value("${imgbb.apikey}")
	private String apiKey;
	
	
	@GetMapping("/all")
	public ResponseEntity<?> getAllProds(
			@RequestParam(required = false) String category,
			@RequestParam(required = false) String searchTitle,
			@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
		try {
			Sort sort = Sort.by(Sort.Direction.ASC, "title");
			Pageable pageable = PageRequest.of(page - 1, size, sort); // PageRequest 是 0-based
			
			if (category!=null && category.equals("全部產品")) {
				category = null;
			}
			Page<Product> pagedResult = productService.getAllProds(pageable, category, searchTitle);
			
//			pagedResult.getContent()：回傳的是 List<Product>（單頁的產品清單）
//			.stream()：將 List 轉換成 Java Stream，可以進行一連串轉換操作
//			.map(product -> this.mapToDto(product))：用 .map() 對 stream 中的每個 Product 元素執行 mapToDto 方法，將 Product 轉成 ProductDTO
//			.toList()：把轉換後的 Stream<ProductDTO> 收集成 List<ProductDTO>
			List<ProductDTO> productDTOs = pagedResult.getContent().stream()
		            .map(product -> this.mapToDto(product))
		            .toList();
			
			PaginationDTO paginationDTO = new PaginationDTO();
	        paginationDTO.setTotal_pages(pagedResult.getTotalPages());
	        paginationDTO.setCurrent_page(page);
	        paginationDTO.setHas_pre(pagedResult.hasPrevious());
	        paginationDTO.setHas_next(pagedResult.hasNext());
	        paginationDTO.setCategory("");
	        
	        Map<String, Object> response = new HashMap<>();
	        response.put("success", true);
	        response.put("products", productDTOs);
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
	
	@PostMapping("/product")
	public ResponseEntity<?> insertProduct(
	    @RequestParam("product") String productJson,
	    @RequestParam(value = "mainImageFile", required = false) MultipartFile mainImageFile,
	    @RequestParam(value = "subImageFiles", required = false) MultipartFile[] subImageFiles
	) {
	    try {
	        ObjectMapper objectMapper = new ObjectMapper();
	        ProductDTO dto = objectMapper.readValue(productJson, ProductDTO.class);

	        List<String> messageList = new ArrayList<>();
	        if (dto.getTitle() == null || dto.getTitle().isBlank()) messageList.add("title 屬性不得為空");
	        if (dto.getCategory() == null || dto.getCategory().isBlank()) messageList.add("category 屬性不得為空");
	        if (dto.getUnit() == null || dto.getUnit().isBlank()) messageList.add("unit 屬性不得為空");

	        if (!messageList.isEmpty()) {
	            Map<String, Object> errorResponse = new HashMap<>();
	            errorResponse.put("success", false);
	            errorResponse.put("message", messageList);
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	        }

	        // ImgBB API Key (注入)
	        String uploadUrl = "https://api.imgbb.com/1/upload?key=" + apiKey;

	        RestTemplate restTemplate = new RestTemplate();
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

	        // 上傳主圖
	        if (mainImageFile != null && !mainImageFile.isEmpty()) {
	            String mainImageBase64 = Base64.getEncoder().encodeToString(mainImageFile.getBytes());
	            MultiValueMap<String, String> mainForm = new LinkedMultiValueMap<>();
	            mainForm.add("image", mainImageBase64);

	            HttpEntity<MultiValueMap<String, String>> mainRequest = new HttpEntity<>(mainForm, headers);
	            ResponseEntity<Map<String, Object>> res = restTemplate.exchange(
	                    uploadUrl,
	                    HttpMethod.POST,
	                    mainRequest,
	                    new ParameterizedTypeReference<Map<String, Object>>() {}
	                );

	            Map<String, Object> body = res.getBody();

	            if (body != null) {
	                Object dataObj = body.get("data");
	                if (dataObj instanceof Map<?, ?> dataMap) {
	                    String url = (String) dataMap.get("url");
	                    dto.setImageUrl(url);
	                }
	            }
	        }

	        // 上傳多圖
	        if (subImageFiles != null && subImageFiles.length > 0) {
	            List<String> subImageUrls = new ArrayList<>();
	            for (MultipartFile file : subImageFiles) {
	                if (file != null && !file.isEmpty()) {
	                    String base64 = Base64.getEncoder().encodeToString(file.getBytes());
	                    MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
	                    form.add("image", base64);

	                    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);
	                    ResponseEntity<Map<String, Object>> res = restTemplate.exchange(
	    	                    uploadUrl,
	    	                    HttpMethod.POST,
	    	                    request,
	    	                    new ParameterizedTypeReference<Map<String, Object>>() {}
	    	                );

	    	            Map<String, Object> body = res.getBody();

	    	            if (body != null) {
	    	                Object dataObj = body.get("data");
	    	                if (dataObj instanceof Map<?, ?> dataMap) {
	    	                    String url = (String) dataMap.get("url");
	    	                    subImageUrls.add(url);
	    	                }
	    	            }
	                }
	            }
	            dto.setImagesUrl(subImageUrls);
	        }

	        // 轉成實體並存資料庫
	        Product prod = mapToEntity(dto);
	        productService.insertProduct(prod);

	        Map<String, Object> response = new HashMap<>();
	        response.put("success", true);
	        response.put("message", "新增成功");
	        response.put("product", dto);
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        e.printStackTrace();
	        Map<String, Object> errorResponse = new HashMap<>();
	        errorResponse.put("success", false);
	        errorResponse.put("message", "新增失敗");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	    }
	}
	
	@PutMapping("/product/{id}")
	public ResponseEntity<?> updateProduct(
	    @PathVariable String id,
	    @RequestParam("product") String productJson,
	    @RequestParam(value = "mainImageFile", required = false) MultipartFile mainImageFile,
	    @RequestParam(value = "subImageFiles", required = false) MultipartFile[] subImageFiles
	) {
	    try {
	        ObjectMapper objectMapper = new ObjectMapper();
	        ProductDTO dto = objectMapper.readValue(productJson, ProductDTO.class);

	        // Imgbb 上傳圖（與 insert 相同）
	        String uploadUrl = "https://api.imgbb.com/1/upload?key=" + apiKey;

	        RestTemplate restTemplate = new RestTemplate();
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

	        // 主圖
	        if (mainImageFile != null && !mainImageFile.isEmpty()) {
	            String base64 = Base64.getEncoder().encodeToString(mainImageFile.getBytes());
	            MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
	            form.add("image", base64);

	            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);
	            ResponseEntity<Map<String, Object>> res = restTemplate.exchange(
	                    uploadUrl,
	                    HttpMethod.POST,
	                    request,
	                    new ParameterizedTypeReference<Map<String, Object>>() {}
	                );

	            Map<String, Object> body = res.getBody();

	            if (body != null) {
	                Object dataObj = body.get("data");
	                if (dataObj instanceof Map<?, ?> dataMap) {
	                    String url = (String) dataMap.get("url");
	                    dto.setImageUrl(url);
	                }
	            }
	        }

	        // 多圖
	        if (subImageFiles != null && subImageFiles.length > 0) {
	            List<String> urls = new ArrayList<>();
	            for (MultipartFile file : subImageFiles) {
	                if (file != null && !file.isEmpty()) {
	                    String base64 = Base64.getEncoder().encodeToString(file.getBytes());
	                    MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
	                    form.add("image", base64);

	                    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);
	                    ResponseEntity<Map<String, Object>> res = restTemplate.exchange(
	    	                    uploadUrl,
	    	                    HttpMethod.POST,
	    	                    request,
	    	                    new ParameterizedTypeReference<Map<String, Object>>() {}
	    	                );

	    	            Map<String, Object> body = res.getBody();

	    	            if (body != null) {
	    	                Object dataObj = body.get("data");
	    	                if (dataObj instanceof Map<?, ?> dataMap) {
	    	                    String url = (String) dataMap.get("url");
	    	                    urls.add(url);
	    	                }
	    	            }
	                }
	            }
	            dto.setImagesUrl(urls);
	        }

	        // 更新實體資料
	        dto.setId(id); // 加入 ID（如果 DTO 沒帶）
	        Product prod = mapToEntity(dto);
	        productService.updateProduct(prod);

	        Map<String, Object> response = new HashMap<>();
	        response.put("success", true);
	        response.put("message", "更新成功");
	        return ResponseEntity.ok(response);
	    } catch (Exception e) {
	        e.printStackTrace();
	        Map<String, Object> errorResponse = new HashMap<>();
	        errorResponse.put("success", false);
	        errorResponse.put("message", "更新失敗");
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	    }
	}
	
//	@PutMapping("/updateprod/{id}")
//	public ResponseEntity<?> updateProduct(@PathVariable String id, @RequestBody ProductDTO prodDto) {
//		try {
//			Product prod = mapToEntity(prodDto);
//			productService.updateProduct(prod);
//			
//			Map<String, Object> response = new HashMap<>();
//	        response.put("success", true);
//	        response.put("message", "更新成功");
//			return ResponseEntity.ok(response);
//		} catch (Exception e) {
//			Map<String, Object> errorResponse = new HashMap<>();
//            errorResponse.put("success", false);
//            errorResponse.put("message", "新增失敗");
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
//		}
//	}
	
	@DeleteMapping("/product/{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable String id) {
		try {
			productService.deleteProduct(id);
			
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
	
	private ProductDTO mapToDto(Product product) {
		if (product == null) {
	        return null;
	    }
		ProductDTO dto = new ProductDTO();
		dto.setId(product.getId());
		dto.setTitle(product.getTitle());
		dto.setCategory(product.getCategory());
		dto.setContent(product.getContent());
		dto.setDescription(product.getDescription());
		dto.setImageUrl(product.getImageUrl());
		dto.setImagesUrl(product.getImagesUrl());
		dto.setIsEnabled(product.getIsEnabled());
		dto.setOriginPrice(product.getOriginPrice());
		dto.setPrice(product.getPrice());
		dto.setUnit(product.getUnit());
		dto.setNum(product.getNum());
		return dto;
	}
	private Product mapToEntity(ProductDTO dto) {
		if (dto == null) {
			return null;
		}
		Product prod = new Product();
		prod.setId(dto.getId());
		prod.setTitle(dto.getTitle());
		prod.setCategory(dto.getCategory());
		prod.setContent(dto.getContent());
		prod.setDescription(dto.getDescription());
		prod.setImageUrl(dto.getImageUrl());
		prod.setImagesUrl(dto.getImagesUrl());
		prod.setIsEnabled(dto.getIsEnabled());
		prod.setOriginPrice(dto.getOriginPrice());
		prod.setPrice(dto.getPrice());
		prod.setUnit(dto.getUnit());
		prod.setNum(dto.getNum());
		return prod;
	}
	
	@GetMapping("/id/{id}")
	public ResponseEntity<?> getProdsByTitle(@PathVariable String id) {
		try {
			
			Optional<Product> prodOpt = productService.getProdById(id);
			if (prodOpt.isPresent()) {
				Product prod = prodOpt.get();
				ProductDTO dto = this.mapToDto(prod);
				
				Map<String, Object> response = new HashMap<>();
		        response.put("success", true);
		        response.put("product", dto);
		        return ResponseEntity.ok(response);
			}else {
				Map<String, Object> errorResponse = new HashMap<>();
	            errorResponse.put("success", false);
	            errorResponse.put("message", "無此商品");
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
			}
		}catch (Exception e) {
			Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "程式錯誤");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
		}
	}
	
//	@PostMapping("/insertprod")
//  public ResponseEntity<?> insertProduct(@RequestBody ProductDTO dto) {
//		try {
//			if (dto.getTitle().isBlank() || dto.getCategory().isBlank() || dto.getUnit().isBlank()) {
//				List<String> messageList = new ArrayList<>();;
//				if(dto.getTitle().isBlank()) messageList.add("title 屬性不得為空");
//				if(dto.getCategory().isBlank()) messageList.add("category 屬性不得為空");
//				if(dto.getUnit().isBlank()) messageList.add("unit 屬性不得為空");
//				
//				Map<String, Object> errorResponse = new HashMap<>();
//	            errorResponse.put("success", false);
//	            errorResponse.put("message", messageList);
//	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
//			}
//			Product prod = mapToEntity(dto);
//			productService.insertProduct(prod);
//			
//			Map<String, Object> response = new HashMap<>();
//	        response.put("success", true);
//	        response.put("message", "新增成功");
//			return ResponseEntity.ok(response);
//		} catch (Exception e) {
//			Map<String, Object> errorResponse = new HashMap<>();
//          errorResponse.put("success", false);
//          errorResponse.put("message", "新增失敗");
//          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
//		}
//  }
	
}
