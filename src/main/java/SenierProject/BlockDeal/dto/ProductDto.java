package SenierProject.BlockDeal.dto;

import SenierProject.BlockDeal.entity.Member;
import SenierProject.BlockDeal.entity.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ProductDto {

    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private Member seller;  // seller 객체 추가
    private CategoryDto category;

    public ProductDto(Product product) {
        this.id = product.getId();
        this.title = product.getTitle();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
        this.seller = product.getSeller();  // Member 객체 설정
        this.category = new CategoryDto(product.getCategory());
    }
}
