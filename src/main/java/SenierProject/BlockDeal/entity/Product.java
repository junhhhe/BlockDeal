package SenierProject.BlockDeal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_products")
@Data
public class Product extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String title;
    private String description;
    private BigDecimal price;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private Member seller;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnore // 직렬화에서 제외하여 순환 참조 방지
    private Category category;  // 상품의 카테고리

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
