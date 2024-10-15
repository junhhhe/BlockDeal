package SenierProject.BlockDeal.controller;

import SenierProject.BlockDeal.entity.Review;
import SenierProject.BlockDeal.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/{sellerId}")
    public ResponseEntity<String> leaveReview(@PathVariable Long sellerId,
                                              @RequestBody Review review,
                                              Authentication authentication) {
        // 로그인된 사용자의 username을 가져옴 (구매자 정보)
        String buyerUsername = authentication.getName();

        // 리뷰 작성 로직 호출
        reviewService.leaveReview(buyerUsername, sellerId, review);

        // 성공 메시지 반환
        return ResponseEntity.ok("리뷰가 성공적으로 등록되었습니다.");
    }

    @GetMapping("/{sellerId}")
    public ResponseEntity<?> getReviewsForSeller(@PathVariable Long sellerId) {
        // 특정 판매자에 대한 리뷰 목록을 조회
        var reviews = reviewService.getReviewsBySeller(sellerId);

        // 리뷰 목록 반환
        return ResponseEntity.ok(reviews);
    }
}