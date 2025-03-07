package com.tenco.class_jwt_v01.service;

import com.tenco.class_jwt_v01.domain.User;
import com.tenco.class_jwt_v01.dto.LoginResponseDto;
import com.tenco.class_jwt_v01.dto.RegisterRequestDto;
import com.tenco.class_jwt_v01.mapper.UserMapper;
import com.tenco.class_jwt_v01.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    // DB 조회, 생성, 업데이트
    @Autowired // DI 처리
    private UserMapper userMapper;
    @Autowired
    private JWTUtil jwtUtil;

    @Transactional
    public void register(RegisterRequestDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setRefreshToken(null);
        userMapper.save(user);
    }

    // 로그인
    @Transactional
    public LoginResponseDto login(String username, String password) {
        User user = userMapper.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {

            // DB 이름 있고, 비밀번호가 맞다면 JWT 토큰 발행
            String accessToken = jwtUtil.generateAccessToken(username);
            String refreshToken = jwtUtil.generateRefreshToken(username);

            // 로그인을 했다면 디비에 리프레쉬 토큰 업데이트
            user.setRefreshToken(refreshToken);
            userMapper.updateRefreshToken(user);
            return new LoginResponseDto(accessToken, refreshToken, username);
        }

        // 인증 실패
        return null;
    }

    // 새로운 액세스 토큰 생성 (리프레쉬 토큰 있어야 함)
    public String refreshToken(String refreshToken) {
        // 리프레쉬 토큰에서 사용자 이름을 추출
        String username = jwtUtil.extractUsername(refreshToken);

        // db 조회 -- 사용자가 가지고 있는 리프레쉬 토큰
        User user = userMapper.findByUsername(username);

        // 사용자 존재 ok(DB) && 토큰 검증(유효한지 검사)
        if (user != null
                && jwtUtil.validateToken(refreshToken, username)
                && refreshToken.equals(user.getRefreshToken())) {
            // 문제 없으면 다시 액세스 토큰 발급
            return jwtUtil.generateAccessToken(username);
        }

        return null;
    }
}
