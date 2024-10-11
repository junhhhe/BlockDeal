package SenierProject.BlockDeal.service;

import SenierProject.BlockDeal.entity.Member;
import SenierProject.BlockDeal.entity.Product;
import SenierProject.BlockDeal.entity.Wishlist;
import SenierProject.BlockDeal.exception.OwnProductWishlistException;
import SenierProject.BlockDeal.exception.ProductNotFoundException;
import SenierProject.BlockDeal.exception.WishlistAlreadyExistsException;
import SenierProject.BlockDeal.repository.MemberJpaRepository;
import SenierProject.BlockDeal.repository.ProductRepository;
import SenierProject.BlockDeal.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final ProductRepository productRepository;

    // 사용자가 찜한 모든 상품 목록 조회
    public List<Product> getWishlistByUsername(String username) {
        Member member = memberJpaRepository.findByUsername(username);
        if (member == null) {
            throw new ProductNotFoundException("해당 사용자를 찾을 수 없습니다: " + username);
        }

        List<Wishlist> wishlistItems = wishlistRepository.findByMember(member);
        return wishlistItems.stream()
                .map(Wishlist::getProduct)
                .collect(Collectors.toList());
    }

    // 상품을 찜 목록에 추가
    public void addToWishlist(String username, Long productId) {
        if (productId == null) {
            throw new IllegalArgumentException("상품 ID가 null입니다.");
        }

        Member member = memberJpaRepository.findByUsername(username);
        if (member == null) {
            throw new ProductNotFoundException("해당 사용자를 찾을 수 없습니다: " + username);
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다: " + productId));

        if (wishlistRepository.findByMemberAndProduct(member, product).isPresent()) {
            throw new WishlistAlreadyExistsException("이미 찜한 상품입니다: " + product.getTitle());
        }

        // 사용자가 자신의 상품을 찜하려는 경우 예외 던지기
        if (product.getSeller().getUsername().equals(username)) {
            throw new OwnProductWishlistException("자신의 상품은 찜할 수 없습니다.");
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setMember(member);
        wishlist.setProduct(product);
        wishlistRepository.save(wishlist);
    }

    // 찜 목록에서 상품 제거
    public void removeFromWishlist(String username, Long productId) {
        if (productId == null) {
            throw new IllegalArgumentException("상품 ID가 null입니다.");
        }

        Member member = memberJpaRepository.findByUsername(username);
        if (member == null) {
            throw new ProductNotFoundException("해당 사용자를 찾을 수 없습니다: " + username);
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다: " + productId));

        Wishlist wishlist = wishlistRepository.findByMemberAndProduct(member, product)
                .orElseThrow(() -> new ProductNotFoundException("해당 상품이 찜 목록에 없습니다: " + product.getTitle()));

        wishlistRepository.delete(wishlist);
    }
}
