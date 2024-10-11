package SenierProject.BlockDeal.repository;

import SenierProject.BlockDeal.entity.Category;
import SenierProject.BlockDeal.entity.Member;
import SenierProject.BlockDeal.entity.Product;
import SenierProject.BlockDeal.entity.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // 특정 카테고리에서 판매중인 상품만 필터링
    List<Product> findByCategoryAndStatus(Category category, ProductStatus status);

    // 판매자가 등록한 상품 목록 조회
    List<Product> findBySeller(Member seller);

    // 같은 카테고리 내에서 현재 상품을 제외하고, 최근 등록된 5개의 판매중인 상품 조회
    List<Product> findTop5ByCategoryAndIdNotAndStatusOrderByCreatedAtDesc(
            Category category, Long id, ProductStatus status);
}
