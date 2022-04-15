package com.example.spring.security.repository;

import com.example.spring.security.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    UserInfo findByAccountIdAndAccountPw(String accountId, String accountPw);

}
