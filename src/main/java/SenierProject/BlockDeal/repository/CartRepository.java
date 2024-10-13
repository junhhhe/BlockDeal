package SenierProject.BlockDeal.repository;

import SenierProject.BlockDeal.entity.Cart;
import SenierProject.BlockDeal.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    // 특정 구매자의 장바구니 찾기
    Optional<Cart> findByBuyer(Member buyer);
}
