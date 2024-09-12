package SenierProject.BlockDeal.controller;

import SenierProject.BlockDeal.entity.Category;
import SenierProject.BlockDeal.entity.Product;
import SenierProject.BlockDeal.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    // 카테고리 목록 조회
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = productService.getAllCategories();
        System.out.println("카테고리 목록 요청: " + categories.size() + "개");
        return ResponseEntity.ok(categories);
    }


    // 상품 상세 조회
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        Product product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    // 특정 카테고리에서 판매중인 상품들만 조회
    @GetMapping("/category/{categoryId}/on-sale")
    public ResponseEntity<List<Product>> getOnSaleProductsByCategory(@PathVariable Long categoryId) {
        List<Product> products = productService.getOnSaleProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }

    // 상품 찜하기
    @PostMapping("/{productId}/wishlist")
    public ResponseEntity<String> addToWishlist(@PathVariable Long productId, Authentication authentication) {
        String username = authentication.getName();
        productService.addToWishlist(username, productId);
        return ResponseEntity.ok("상품이 찜 목록에 추가되었습니다.");
    }

    // 상품 등록
    @PostMapping("/register")
    public ResponseEntity<String> registerProduct(@RequestBody Product product, Authentication authentication) {
        String sellerUsername = authentication.getName();
        productService.registerProduct(sellerUsername, product);
        return ResponseEntity.ok("상품이 성공적으로 등록되었습니다.");
    }

    // 판매 완료 처리
    @PutMapping("/{productId}/complete")
    public ResponseEntity<String> completeSale(@PathVariable Long productId) {
        productService.completeSale(productId);
        return ResponseEntity.ok("판매가 완료되었습니다.");
    }

    // **로그인한 사용자가 등록한 상품 목록 조회**
    @GetMapping("/my-products")
    public ResponseEntity<List<Product>> getMyProducts(Authentication authentication) {
        String username = authentication.getName();
        List<Product> products = productService.getProductsBySeller(username);
        return ResponseEntity.ok(products);
    }
}
