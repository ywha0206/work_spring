package com.tenco.class_jwt_v01.controller;

import com.tenco.class_jwt_v01.dto.LoginResponseDto;
import com.tenco.class_jwt_v01.dto.RegisterRequestDto;
import com.tenco.class_jwt_v01.service.UserService;
import com.tenco.class_jwt_v01.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Log4j2
@Controller
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    JWTUtil jwtUtil;

    // 회원가입
    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(RegisterRequestDto dto) {
        // 유효성 검사 생략
        userService.register(dto);
        return "redirect:/login";
    }

    // 로그인
    @GetMapping("/login")
    public String showLogin() {
        return "login";
    }

    // key = 값
    // 로그인 성공시 -> welcome 으로 이동 (토큰) - 리프레쉬 갱신
    // 로그인 실패시 -> login (error = "실패")
    @PostMapping("/login")
    public String login(String username, String password, Model model) {
        LoginResponseDto resp = userService.login(username, password);
        log.info(resp);
        if (resp != null) {
            model.addAttribute("accessToken", resp.getAccessToken());
            model.addAttribute("refreshToken", resp.getRefreshToken());
            model.addAttribute("username", resp.getUsername());
            log.info("로그인 성공 후 " + model.getAttribute("username"));
            return "welcome";
        }
        model.addAttribute("error", "아이디 또는 비밀번호가 맞지 않습니다.");
        return "login";
    }

    @GetMapping("/welcome")
    public String showWelcome() {
        return "welcome";
    }

    @PostMapping("/refresh")
    public String refreshToken(@RequestParam String refreshToken, Model model) {
        String newToken = userService.refreshToken(refreshToken);
        if (newToken != null) {
            String username = jwtUtil.extractUsername(newToken);
            model.addAttribute("username", username);
            model.addAttribute("accessToken", newToken);
            model.addAttribute("refreshToken", refreshToken);
            return "welcome";
        }
        return "redirect:/login";
    }
}
