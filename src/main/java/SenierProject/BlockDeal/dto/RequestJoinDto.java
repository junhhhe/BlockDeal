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

    public Member toEntity(){
        return Member.builder()
                .username(this.username)
                .password(this.password)
                .name(this.name)
                .role(MemberRole.USER)
                .build();
    }
}
