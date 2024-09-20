package SenierProject.BlockDeal.controller;

import SenierProject.BlockDeal.dto.CategoryDto;
import SenierProject.BlockDeal.dto.ProductDto;
import SenierProject.BlockDeal.entity.Category;
import SenierProject.BlockDeal.entity.Product;
import SenierProject.BlockDeal.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    // 카테고리 목록 조회
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<Category> categories = productService.getAllCategories();
        List<CategoryDto> categoryDtos = categories.stream()
                .map(CategoryDto::new)  // Category -> CategoryDto로 변환
                .collect(Collectors.toList());
        return ResponseEntity.ok(categoryDtos);
    }

    // 상품 상세 조회
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long productId) {
        Product product = productService.getProductById(productId);
        ProductDto productDTO = new ProductDto(product);
        return ResponseEntity.ok(productDTO);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long categoryId) {
        Category category = productService.findCategoryById(categoryId);  // 수정된 부분
        CategoryDto categoryDto = new CategoryDto(category);
        return ResponseEntity.ok(categoryDto);
    }

    // 특정 카테고리에서 판매중인 상품들만 조회
    @GetMapping("/category/{categoryId}/on-sale")
    public ResponseEntity<List<ProductDto>> getOnSaleProductsByCategory(@PathVariable Long categoryId) {
        List<Product> products = productService.getOnSaleProductsByCategory(categoryId);
        List<ProductDto> productDtos = products.stream()
                .map(ProductDto::new)  // Product -> ProductDto로 변환
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDtos);
    }

    // 상품 등록
    @PostMapping("/add")
    public ResponseEntity<String> registerProduct(
            @RequestPart("product") ProductDto productDto,
            @RequestPart("imageFile") MultipartFile imageFile,
            Authentication authentication) {

        String sellerUsername = authentication.getName();
        try {
            // 이미지 파일과 상품 등록 처리
            productService.registerProduct(sellerUsername, productDto, imageFile);
            return ResponseEntity.ok("상품이 성공적으로 등록되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("상품 등록 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 판매 완료 처리
    @PutMapping("/{productId}/complete")
    public ResponseEntity<String> completeSale(@PathVariable Long productId) {
        productService.completeSale(productId);
        return ResponseEntity.ok("판매가 완료되었습니다.");
    }

    // 로그인한 사용자가 등록한 상품 목록 조회
    @GetMapping("/my-products")
    public ResponseEntity<List<ProductDto>> getMyProducts(Authentication authentication) {
        String username = authentication.getName();
        List<Product> products = productService.getProductsBySeller(username);
        List<ProductDto> productDtos = products.stream()
                .map(ProductDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDtos);
    }


}
