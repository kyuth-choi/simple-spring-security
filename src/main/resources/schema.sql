DROP TABLE IF EXISTS tb_user_info;

CREATE TABLE tb_user_info (
    `user_no` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '회원번호',
    `account_Id` varchar(100) NOT NULL COMMENT '계정',
    `account_pw` varchar(50) NOT NULL,
    `user_name` varchar(100) DEFAULT NULL,
    `address` varchar(100) DEFAULT NULL,
    `age` int(10) unsigned DEFAULT NULL,
    `user_level` tinyint DEFAULT NULL,
    PRIMARY KEY (`user_no`)
);
