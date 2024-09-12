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
    List<Product> findBySeller(Member seller);  // 판매자가 등록한 상품 목록 조회
}