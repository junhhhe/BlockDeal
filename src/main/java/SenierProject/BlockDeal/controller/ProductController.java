package SenierProject.BlockDeal.controller;

import SenierProject.BlockDeal.dto.CategoryDto;
import SenierProject.BlockDeal.dto.ProductDto;
import SenierProject.BlockDeal.entity.Category;
import SenierProject.BlockDeal.entity.Product;
import SenierProject.BlockDeal.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    // 특정 카테고리 조회
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long categoryId) {
        try {
            Category category = productService.getCategoryById(categoryId);
            CategoryDto categoryDto = new CategoryDto(category);
            return ResponseEntity.ok(categoryDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // 상품 상세 조회
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long productId) {
        Product product = productService.getProductById(productId);
        ProductDto productDTO = new ProductDto(product);
        return ResponseEntity.ok(productDTO);
    }

    // 같은 카테고리의 연관 상품 조회 (5개 최신순)
    @GetMapping("/{productId}/related")
    public ResponseEntity<List<ProductDto>> getRelatedProducts(@PathVariable Long productId) {
        List<Product> relatedProducts = productService.getRelatedProducts(productId);
        List<ProductDto> relatedProductDtos = relatedProducts.stream()
                .map(ProductDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(relatedProductDtos);
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


    // 상품 삭제 API
    @DeleteMapping("/del/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId, Authentication authentication) {
        Product product = productService.getProductById(productId);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 상품을 찾을 수 없습니다.");
        }

        String currentUsername = authentication != null ? authentication.getName() : null;
        if (currentUsername == null || !currentUsername.equals(product.getSeller().getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("상품 삭제 권한이 없습니다.");
        }

        productService.deleteProduct(productId);
        return ResponseEntity.ok("상품이 성공적으로 삭제되었습니다.");
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
