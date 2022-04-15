package com.example.spring.security.controller;

import com.example.spring.security.vo.LoginInfo;
import com.example.spring.security.vo.ResponseInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/admin")
public class RoleTestController {

    @GetMapping(value = "/get")
    public ResponseInfo get(@AuthenticationPrincipal LoginInfo loginInfo) {
        log.info(loginInfo.toString());
        return new ResponseInfo();
    }

    @PostMapping(value = "/post")
    public ResponseInfo post(@AuthenticationPrincipal LoginInfo loginInfo) {
        log.info(loginInfo.toString());
        return new ResponseInfo();
    }

}
