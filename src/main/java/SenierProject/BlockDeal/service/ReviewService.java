package SenierProject.BlockDeal.service;

import SenierProject.BlockDeal.entity.Member;
import SenierProject.BlockDeal.entity.Review;
import SenierProject.BlockDeal.repository.MemberJpaRepository;
import SenierProject.BlockDeal.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MemberJpaRepository memberRepository;

    // 리뷰 남기기
    public void leaveReview(String buyerUsername, Long sellerId, Review review) {
        Member buyer = memberRepository.findByUsername(buyerUsername);
        Member seller = memberRepository.findById(sellerId)
                .orElseThrow(() -> new IllegalArgumentException("판매자를 찾을 수 없습니다."));

        review.setBuyer(buyer);
        review.setSeller(seller);
        reviewRepository.save(review);
    }
}