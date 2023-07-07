package com.sparta.lv2.service;

import com.sparta.lv2.dto.UserRequestDto;
import com.sparta.lv2.entity.User;
import com.sparta.lv2.jwt.JwtUtil;
import com.sparta.lv2.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final HttpServletResponse httpServletResponse;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, HttpServletResponse httpServletResponse) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.httpServletResponse = httpServletResponse;
    }

    public ResponseEntity<String> signup(UserRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        Optional<User> findUser = userRepository.findByUsername(username);
        if (findUser.isPresent()) {
            throw new IllegalArgumentException("중복된 이름입니다.");
        }

        User user = new User(username, password);
        userRepository.save(user);

        return new ResponseEntity<>("msg : 로그인 성공.", HttpStatus.OK);
    }

    public ResponseEntity<String> login(UserRequestDto requestDto) {
        User user = userRepository.findByUsername(requestDto.getUsername()).orElseThrow(() ->
                    new NullPointerException("해당 유저는 존재하지 않습니다.")
        );
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())){
            // false => 예외처리
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        } else {
            // true => JWT 생성
            String token = jwtUtil.createToken(user.getId(), user.getUsername());
            // header에 저장
            httpServletResponse.setHeader("Authorization", token);
            // ResponseDto에 msg, statusCode 반환
            return new ResponseEntity<>("msg : 로그인 성공.", HttpStatus.OK);
        }
    }
}
