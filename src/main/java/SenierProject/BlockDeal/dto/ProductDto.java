package SenierProject.BlockDeal.dto;

import SenierProject.BlockDeal.entity.Category;
import SenierProject.BlockDeal.entity.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ProductDto {

    private Long productId;
    private String title;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private String sellerName;
    private CategoryDto category;

    public ProductDto(Product product) {
        this.productId = product.getProductId();
        this.title = product.getTitle();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
        this.sellerName = product.getSeller().getName();
        this.category = new CategoryDto(product.getCategory());
    }
}
