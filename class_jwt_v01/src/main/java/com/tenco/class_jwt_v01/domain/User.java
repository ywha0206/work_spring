package com.tenco.class_jwt_v01.domain;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String username;
    private String password; // 실제로는 암호화 필요
    // 액세스 토큰 디비에 저장 안 함 10분
    //디비 저장 액세스 토큰이 만료되었을 때 새 액세스 토큰 발급받기 위해 사용 7일
    private String refreshToken;
}
