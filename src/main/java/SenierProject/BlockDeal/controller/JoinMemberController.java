package SenierProject.BlockDeal.controller;

import SenierProject.BlockDeal.dto.RequestMemberDto;
import SenierProject.BlockDeal.service.JoinService;
import SenierProject.BlockDeal.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@AllArgsConstructor
public class JoinMemberController {

    private final JoinService joinService;

    @PostMapping("/join")
    public String joinProcess(RequestMemberDto requestMemberDto){

        joinService.joinProcess(requestMemberDto);

        return "ok";
    }
}
