package SenierProject.BlockDeal.service;

import SenierProject.BlockDeal.entity.Category;
import SenierProject.BlockDeal.entity.Product;
import SenierProject.BlockDeal.entity.ProductStatus;
import SenierProject.BlockDeal.entity.Wishlist;
import SenierProject.BlockDeal.entity.Member;
import SenierProject.BlockDeal.repository.ProductRepository;
import SenierProject.BlockDeal.repository.WishlistRepository;
import SenierProject.BlockDeal.repository.MemberJpaRepository;
import SenierProject.BlockDeal.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final WishlistRepository wishlistRepository;
    private final MemberJpaRepository memberRepository;
    private final CategoryRepository categoryRepository;

    // 카테고리 목록 조회
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // 상품 상세 정보 가져오기
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
    }

    // 찜 목록에 추가
    public void addToWishlist(String username, Long productId) {
        Member member = memberRepository.findByUsername(username);
        Product product = getProductById(productId);

        Optional<Wishlist> existingWishlist = wishlistRepository.findByMemberAndProduct(member, product);
        if (existingWishlist.isPresent()) {
            throw new IllegalArgumentException("이미 찜한 상품입니다.");
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setMember(member);
        wishlist.setProduct(product);
        wishlistRepository.save(wishlist);
    }

    // 특정 카테고리에서 판매중인 상품들만 조회
    public List<Product> getOnSaleProductsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));
        return productRepository.findByCategoryAndStatus(category, ProductStatus.ON_SALE);
    }

    //상품등록
    @Transactional
    public void registerProduct(String sellerUsername, Product product) {
        // 판매자를 찾기 위한 로직
        Member seller = Optional.ofNullable(memberRepository.findByUsername(sellerUsername))
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        // 상품에 판매자 설정
        product.setSeller(seller);
        product.setStatus(ProductStatus.ON_SALE);

        // 상품 저장
        productRepository.save(product);
    }

    //상품판매
    @Transactional
    public void completeSale(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다."));

        // 판매 완료 처리
        product.setStatus(ProductStatus.SOLD_OUT);

        productRepository.save(product);  // 상태 업데이트
    }

    // **로그인한 사용자가 등록한 상품 목록 조회**
    public List<Product> getProductsBySeller(String sellerUsername) {
        Member seller = memberRepository.findByUsername(sellerUsername);
        return productRepository.findBySeller(seller);
    }
}
