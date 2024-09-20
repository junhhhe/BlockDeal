package SenierProject.BlockDeal.controller;

import SenierProject.BlockDeal.dto.WishlistDto;
import SenierProject.BlockDeal.entity.Product;
import SenierProject.BlockDeal.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    // 찜 목록에 상품 추가
    @PostMapping
    public ResponseEntity<String> addToWishlist(@RequestBody WishlistDto request) {
        wishlistService.addToWishlist(request.getUsername(), request.getProductId());
        return ResponseEntity.ok("Added to wishlist");
    }

    // 사용자의 찜한 상품 목록 조회
    @GetMapping("/{username}")
    public ResponseEntity<List<Product>> getWishlist(@PathVariable String username) {
        List<Product> wishlistProducts = wishlistService.getWishlistByUsername(username);
        return ResponseEntity.ok(wishlistProducts);
    }

    // 찜 목록에서 상품 삭제 (선택적으로 추가)
    @DeleteMapping
    public ResponseEntity<String> removeFromWishlist(@RequestBody WishlistDto request) {
        wishlistService.removeFromWishlist(request.getUsername(), request.getProductId());
        return ResponseEntity.ok("Removed from wishlist");
    }
}
