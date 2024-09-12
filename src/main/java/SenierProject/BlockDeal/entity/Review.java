package SenierProject.BlockDeal.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tbl_reviews")
@Data
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    private String content;
    private int rating; //평점

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Member seller;

    @ManyToOne
    @JoinColumn(name = "buyer_id")
    private Member buyer;

    // Product와의 관계 추가
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}