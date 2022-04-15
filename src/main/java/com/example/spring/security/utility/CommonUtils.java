package com.example.spring.security.utility;

import javax.servlet.http.HttpServletRequest;

public class CommonUtils {
    // Client IP 확인

    public static String getAuthRedisKey(String saverId, String memberId, String userId) {
        if (userId.equals("")) {
            // 운영자
            return "saverapi:user:" + saverId + ":" + memberId + ":" + memberId;
        } else {
            // 부운영자
            return "saverapi:user:" + saverId + ":" + memberId + ":" + userId;
        }
    }

}
