package SenierProject.BlockDeal.repository;

import SenierProject.BlockDeal.entity.Member;
import SenierProject.BlockDeal.entity.Product;
import SenierProject.BlockDeal.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    // 특정 회원이 찜한 상품 조회
    List<Wishlist> findByMember(Member member);

    // 회원이 특정 상품을 이미 찜했는지 확인
    Optional<Wishlist> findByMemberAndProduct(Member member, Product product);
}