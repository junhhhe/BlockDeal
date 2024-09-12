package SenierProject.BlockDeal.service;

import SenierProject.BlockDeal.entity.Member;
import SenierProject.BlockDeal.entity.Product;
import SenierProject.BlockDeal.entity.Wishlist;
import SenierProject.BlockDeal.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;

    // 사용자가 찜한 모든 상품 목록을 조회
    public List<Wishlist> getWishlistForMember(Member member) {
        return wishlistRepository.findByMember(member);
    }

    // 상품을 찜에 추가
    public Wishlist addToWishlist(Member member, Product product) {
        if (wishlistRepository.findByMemberAndProduct(member, product).isEmpty()) {
            Wishlist wishlist = new Wishlist();
            wishlist.setMember(member);
            wishlist.setProduct(product);
            return wishlistRepository.save(wishlist);
        }
        return null; // 이미 찜한 경우 처리
    }
}