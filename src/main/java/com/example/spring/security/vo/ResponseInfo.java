package com.example.spring.security.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResponseInfo {
    private int responseCode = 0;
    private String responseMsg = "success";
    private Object data;
}
