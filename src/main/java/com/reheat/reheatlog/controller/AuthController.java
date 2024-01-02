package com.reheat.reheatlog.controller;

import com.reheat.reheatlog.config.AppConfig;
import com.reheat.reheatlog.request.Signup;
import com.reheat.reheatlog.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final AppConfig appConfig;

    @GetMapping("/auth/login")
    public String login() {
        return "로그인 페이지입니다";
    }

    @PostMapping("/auth/signup")
    public void singup(@RequestBody Signup signup) {
        authService.signup(signup);
    }
}
