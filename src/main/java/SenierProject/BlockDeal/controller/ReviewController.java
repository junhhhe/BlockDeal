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

    // 리뷰 남기기
    @PostMapping("/{sellerId}")
    public ResponseEntity<String> leaveReview(@PathVariable Long sellerId, @RequestBody Review review, Authentication authentication) {
        String buyerUsername = authentication.getName();
        reviewService.leaveReview(buyerUsername, sellerId, review);
        return ResponseEntity.ok("리뷰가 등록되었습니다.");
    }
}