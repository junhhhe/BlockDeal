package SenierProject.BlockDeal.entity;

import SenierProject.BlockDeal.jwt.MemberRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "tbl_members")
public class Member extends Base{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ID가 자동으로 증가하도록 설정
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "email", nullable = false)
    private String email;

    // 프로필 이미지 URL 필드 추가
    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private MemberRole role;

    // 평균 평점 필드 추가
    @Column(name = "average_rating", nullable = true)
    private Double averageRating;
}
