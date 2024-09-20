package SenierProject.BlockDeal.service;

import SenierProject.BlockDeal.entity.Member;
import SenierProject.BlockDeal.entity.Product;
import SenierProject.BlockDeal.entity.Wishlist;
import SenierProject.BlockDeal.repository.MemberJpaRepository;
import SenierProject.BlockDeal.repository.ProductRepository;
import SenierProject.BlockDeal.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final ProductRepository productRepository;

    // 사용자가 찜한 모든 상품 목록 조회
    public List<Product> getWishlistByUsername(String username) {
        Member member = memberJpaRepository.findByUsername(username);
        List<Wishlist> wishlistItems = wishlistRepository.findByMember(member);
        // Wishlist 객체의 product 필드를 추출하여 리스트로 반환
        return wishlistItems.stream()
                .map(Wishlist::getProduct)
                .collect(Collectors.toList());
    }

    // 상품을 찜 목록에 추가
    public Wishlist addToWishlist(String username, Long productId) {
        Member member = memberJpaRepository.findByUsername(username);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        // 이미 찜한 상품이 있는지 확인
        if (wishlistRepository.findByMemberAndProduct(member, product).isEmpty()) {
            Wishlist wishlist = new Wishlist();
            wishlist.setMember(member);
            wishlist.setProduct(product);
            return wishlistRepository.save(wishlist);  // 찜에 상품 추가 후 저장
        }

        // 이미 찜한 상품인 경우 예외 처리
        throw new IllegalArgumentException("이미 찜한 상품입니다.");
    }

    // 찜 목록에서 상품 제거
    public void removeFromWishlist(String username, Long productId) {
        Member member = memberJpaRepository.findByUsername(username);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        // 해당 상품이 찜 목록에 있는지 확인
        Wishlist wishlist = wishlistRepository.findByMemberAndProduct(member, product)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품이 찜 목록에 없습니다."));

        wishlistRepository.delete(wishlist);  // 찜 목록에서 제거
    }
}
