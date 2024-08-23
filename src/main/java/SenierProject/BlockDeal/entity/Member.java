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
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // ID가 자동으로 증가하도록 설정
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private MemberRole role;
}
