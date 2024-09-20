package SenierProject.BlockDeal.repository;

import SenierProject.BlockDeal.entity.Member;
import SenierProject.BlockDeal.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findBySeller(Member member);

    // 판매자의 평균 평점을 계산하는 쿼리
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.seller = :seller")
    Double findAverageRatingBySeller(Member seller);
}