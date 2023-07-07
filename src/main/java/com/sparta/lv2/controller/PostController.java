package com.sparta.lv2.controller;

import com.sparta.lv2.dto.PostRequestDto;
import com.sparta.lv2.dto.PostResponseDto;
import com.sparta.lv2.entity.Post;
import com.sparta.lv2.jwt.JwtUtil;
import com.sparta.lv2.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {
    private final PostService postService;
    private final JwtUtil jwtUtil;

    public PostController(JwtUtil jwtUtil, PostService postService) {
        this.jwtUtil = jwtUtil;
        this.postService = postService;
    }

    // 게시글 작성 API
    @PostMapping("/post")
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto, HttpServletRequest httpServletRequest) {
        String token = jwtUtil.jwtSubstring(httpServletRequest.getHeader("Authorization")); // Bear 짜른 토큰 값
        ; // 토큰 검증 결과가 담김 (true or false)

        // 토큰 검증 성공 시 jwt에서 username 뽑고, service에 requestDto와 username 전달
        if (jwtUtil.validateToken(token)) {
            String username = jwtUtil.getUserInfo(token); // jwt에서 username 가져오기
            return postService.createPost(requestDto, username);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    // 전체 게시글 조회 API
    @GetMapping("/posts")
    public List<PostResponseDto> getAllPosts() {
        return postService.getAllposts();
    }

    // 선택한 게시글 조회 API
    @GetMapping("/post/{id}")
    public PostResponseDto getSelectPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

//    // 게시글 수정 API
    @PutMapping("/post/{id}")
    public PostResponseDto updatePost(@PathVariable Long id, @RequestBody PostRequestDto requestDto, HttpServletRequest httpServletRequest) {
        // jwt 유효한지 검증
        String token = jwtUtil.jwtSubstring(httpServletRequest.getHeader("Authorization"));
        if (jwtUtil.validateToken(token)){
            String username = jwtUtil.getUserInfo(token); // jwt에서 username 가져오기
            return postService.updatePost(requestDto, username, id);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

//    // 게시글 삭제 API
    @DeleteMapping("/post/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id, HttpServletRequest httpServletRequest) {
        String token = jwtUtil.jwtSubstring(httpServletRequest.getHeader("Authorization"));
        if (jwtUtil.validateToken(token)){
            String username = jwtUtil.getUserInfo(token); // jwt에서 username 가져오기
            return postService.deletePost(username, id);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
}
