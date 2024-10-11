package SenierProject.BlockDeal.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tbl_wishlist")
@Data
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishlistId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}