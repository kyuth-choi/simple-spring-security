package com.example.spring.security.vo;

import com.example.spring.security.entity.UserInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class LoginInfo {
    private Long userNo;
    private String id;
    private String userName;
    private String roles;
    private String token;

    public LoginInfo() {
    }

    public LoginInfo(UserInfo userInfo) {
        this.userNo = userInfo.getUserNo();
        this.id = userInfo.getAccountId();
        this.userName = userInfo.getUserName();
    }
}
