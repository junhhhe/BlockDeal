package SenierProject.BlockDeal.service;

import SenierProject.BlockDeal.entity.Member;
import SenierProject.BlockDeal.entity.Review;
import SenierProject.BlockDeal.repository.MemberJpaRepository;
import SenierProject.BlockDeal.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberJpaRepository memberRepository;

    // 리뷰 남기기
    @Transactional
    public void leaveReview(String buyerUsername, Long sellerId, Review review) {
        // 구매자 확인
        Member buyer = memberRepository.findByUsername(buyerUsername);
        if (buyer == null) {
            throw new IllegalArgumentException("구매자를 찾을 수 없습니다.");
        }

        // 판매자 확인
        Member seller = memberRepository.findById(sellerId)
                .orElseThrow(() -> new IllegalArgumentException("판매자를 찾을 수 없습니다."));

        // 리뷰 유효성 체크
        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new IllegalArgumentException("별점은 1에서 5 사이여야 합니다.");
        }

        if (review.getComment() == null || review.getComment().trim().isEmpty()) {
            throw new IllegalArgumentException("리뷰 내용을 작성해야 합니다.");
        }

        // 리뷰 작성
        review.setBuyer(buyer);
        review.setSeller(seller);
        reviewRepository.save(review);

        // 판매자 평균 평점 업데이트
        updateSellerRating(seller);
    }

    // 특정 판매자에 대한 리뷰 목록 조회
    public List<Review> getReviewsBySeller(Long sellerId) {
        Member seller = memberRepository.findById(sellerId)
                .orElseThrow(() -> new IllegalArgumentException("판매자를 찾을 수 없습니다."));
        return reviewRepository.findBySeller(seller);
    }

    // 판매자의 평균 평점 업데이트
    @Transactional
    public void updateSellerRating(Member seller) {
        Double averageRating = reviewRepository.findAverageRatingBySeller(seller);
        seller.setAverageRating(averageRating);
        memberRepository.save(seller);
    }
}
