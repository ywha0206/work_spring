package com.tenco.class_jwt_v01.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor // 생성자 한개 생성
public class LoginResponseDto {
    // 액세스토큰
    private String accessToken;
    // 리프레시 토큰
    private String refreshToken;
    // 사용자 이름
    private String username;
    // 나머지 추가적인 정보가 필요하다면 토큰을 확인하고 별도에 API로 제공하는 것을 권장
}
