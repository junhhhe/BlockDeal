/*
package SenierProject.BlockDeal.service;

import SenierProject.BlockDeal.entity.Cart;
import SenierProject.BlockDeal.entity.Member;
import SenierProject.BlockDeal.entity.Product;
import SenierProject.BlockDeal.repository.CartRepository;
import SenierProject.BlockDeal.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public Cart addProductToCart(Long buyerId, Long productId) {
        Member buyer = memberJpaRepository.findByIdAndRole(buyerId, Role.BUYER)
                .orElseThrow(() -> new IllegalArgumentException("구매자를 찾을 수 없습니다."));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));

        Cart cart = cartRepository.findByBuyer(buyer)
                .orElseGet(() -> new Cart(buyer));
        cart.getProducts().add(product);
        return cartRepository.save(cart);
    }
}
*/
