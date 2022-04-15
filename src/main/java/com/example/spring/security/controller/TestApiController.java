package com.example.spring.security.controller;

import com.example.spring.security.entity.UserInfo;
import com.example.spring.security.vo.LoginInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
public class TestApiController {

    @GetMapping(value = "/get")
    public String getTest() {
        return "success";
    }

    @PostMapping(value = "/post")
    public String post() {
        return "success";
    }

}
