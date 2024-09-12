package SenierProject.BlockDeal.dto;

import SenierProject.BlockDeal.entity.Member;
import SenierProject.BlockDeal.jwt.MemberRole;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestJoinDto {

    private String username;
    private String password;
    private String passwordCheck;
    private String name;
    private String nickname;
    private String email;

    public Member toEntity(){
        return Member.builder()
                .username(this.username)
                .password(this.password)
                .name(this.name)
                .nickname(this.nickname)
                .email(this.email)
                .role(MemberRole.USER)
                .build();
    }
}
