package SenierProject.BlockDeal.repository;

import SenierProject.BlockDeal.entity.Product;
import SenierProject.BlockDeal.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // 특정 상품에 대한 리뷰 조회
    List<Review> findByProduct(Product product);
}