package com.sparta.lv2.controller;

import com.sparta.lv2.dto.UserRequestDto;
import com.sparta.lv2.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입 API
    @PostMapping("/auth/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid UserRequestDto requestDto){
        // 정규식 통과 못하면 MethodArgumentNotValidException 발생

        return userService.signup(requestDto);
    }

//     로그인 API
    @PostMapping("/auth/login")
    public ResponseEntity<String> login(@RequestBody UserRequestDto requestDto){
        return userService.login(requestDto);
    }

}
