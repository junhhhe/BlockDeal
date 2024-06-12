package SenierProject.BlockDeal.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ResponseBody
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/")
    public String adminP(){
        return "admin Controller";
    }
}
