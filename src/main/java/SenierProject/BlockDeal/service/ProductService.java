package SenierProject.BlockDeal.service;

import SenierProject.BlockDeal.dto.ProductDto;
import SenierProject.BlockDeal.entity.*;
import SenierProject.BlockDeal.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final MemberJpaRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final FileUploadService fileUploadService;

    // 카테고리 목록 조회
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // 상품 상세 정보 가져오기
    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
    }

    // 특정 카테고리 조회 추가
    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));
    }

    @Transactional
    public void registerProduct(String sellerUsername, ProductDto productDto, MultipartFile imageFile) throws IOException {
        // 판매자 조회
        Member seller = Optional.ofNullable(memberRepository.findByUsername(sellerUsername))
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));

        // 카테고리 조회 - ProductDto에 Category 정보가 포함되어 있다고 가정
        if (productDto.getCategory() == null || productDto.getCategory().getId() == null) {
            throw new IllegalArgumentException("카테고리 정보가 필요합니다.");
        }
        Category category = categoryRepository.findById(productDto.getCategory().getId())
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

        // ProductDto를 Product로 변환
        Product product = new Product();
        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setCategory(category);
        product.setSeller(seller);
        product.setStatus(ProductStatus.ON_SALE);

        // 파일 업로드 처리
        if (!imageFile.isEmpty()) {
            String fileName = fileUploadService.saveFile(imageFile);
            product.setImageUrl(fileName);
        }

        // 상품 저장
        productRepository.save(product);
    }


    // 같은 카테고리의 연관 상품 조회 (현재 상품 제외, 5개 최신순)
    public List<Product> getRelatedProducts(Long productId) {
        Product currentProduct = getProductById(productId);
        return productRepository.findTop5ByCategoryAndIdNotAndStatusOrderByCreatedAtDesc(
                currentProduct.getCategory(), productId, ProductStatus.ON_SALE);
    }

    public List<Product> getOnSaleProductsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("카테고리를 찾을 수 없습니다."));

        // 해당 카테고리의 판매중인 상품들만 조회
        return productRepository.findByCategoryAndStatus(category, ProductStatus.ON_SALE);
    }

    // 상품 판매 완료 처리
    @Transactional
    public void completeSale(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다."));

        // 판매 완료 처리
        product.setStatus(ProductStatus.SOLD_OUT);

        productRepository.save(product);  // 상태 업데이트
    }

    // 로그인한 사용자가 등록한 상품 목록 조회
    public List<Product> getProductsBySeller(String sellerUsername) {
        Member seller = memberRepository.findByUsername(sellerUsername);
        return productRepository.findBySeller(seller);
    }

    //상품삭제
    @Transactional
    public void deleteProduct(Long productId) {
        Optional<Product> productOpt = productRepository.findById(productId);
        if (!productOpt.isPresent()) {
            throw new IllegalArgumentException("해당 상품을 찾을 수 없습니다.");
        }
        Product product = productOpt.get();
        productRepository.delete(product);
    }

}
