package com.example.spring.security.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigInteger;

@Getter
@Setter
@ToString
@Entity
@Table(name = "tb_user_info")
public class UserInfo {

    @Id
    @Column(name = "user_no")
    private Long userNo;
    @Column(name = "account_id")
    private String accountId;
    @Column(name = "account_pw")
    private String accountPw;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "address")
    private String address;
    @Column(name = "age")
    private int age;
    @Column(name="user_level")
    private int userLevel;

}
