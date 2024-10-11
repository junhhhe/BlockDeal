package SenierProject.BlockDeal.controller;

import SenierProject.BlockDeal.dto.WishlistDto;
import SenierProject.BlockDeal.dto.ApiResponse;
import SenierProject.BlockDeal.dto.ProductDto;
import SenierProject.BlockDeal.entity.Product;
import SenierProject.BlockDeal.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    // 찜 목록에 상품 추가
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<String>> addToWishlist(@RequestBody WishlistDto request, Authentication authentication) {
        String username = authentication.getName();
        wishlistService.addToWishlist(username, request.getProductId());
        return ResponseEntity.ok(new ApiResponse<>(true, "찜 목록에 추가되었습니다.", null));
    }

    // 사용자의 찜한 상품 목록 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDto>>> getWishlist(Authentication authentication) {
        String username = authentication.getName();
        List<Product> wishlistProducts = wishlistService.getWishlistByUsername(username);
        List<ProductDto> productDtos = wishlistProducts.stream()
                .map(ProductDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(true, "찜 목록 조회 성공", productDtos));
    }

    // 찜 목록에서 상품 삭제 (Path Variable 사용)
    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<ApiResponse<String>> removeFromWishlist(@PathVariable Long productId, Authentication authentication) {
        String username = authentication.getName();
        wishlistService.removeFromWishlist(username, productId);
        return ResponseEntity.ok(new ApiResponse<>(true, "찜 목록에서 제거되었습니다.", null));
    }
}
