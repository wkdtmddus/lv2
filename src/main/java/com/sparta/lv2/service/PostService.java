package com.sparta.lv2.service;

import com.sparta.lv2.dto.PostRequestDto;
import com.sparta.lv2.dto.PostResponseDto;
import com.sparta.lv2.entity.Post;
import com.sparta.lv2.repository.PostRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository; // PostService의 멤버(생성자 주입)

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // 게시글 작성
    public PostResponseDto createPost(PostRequestDto requestDto, String username) {
        // RequestDto => entity
        Post post = new Post(requestDto, username);

        // entity => save()
        postRepository.save(post);

        // entity => responseDto
        PostResponseDto postResponseDto = new PostResponseDto(post);
        return postResponseDto;
    }

    // 전체 게시글 조회
    public List<PostResponseDto> getAllposts() {
        List<PostResponseDto> allPost = postRepository.findAllByOrderByCreatedAtDesc()
                .stream().map(PostResponseDto::new).toList();
        return allPost;

    }

    // 선택한 게시글 조회
    public PostResponseDto getPost(Long id) {
        Post post = findPost(id);
        PostResponseDto responseDto = new PostResponseDto(post);

        return responseDto;
    }
    @Transactional
    public PostResponseDto updatePost(PostRequestDto requestDto, String username, Long id) {
        // 게시글 가져오기
        Post post = findPost(id);
        // 가져온 게시글의 username(db)과 username(jwt) 비교
        if (!post.getUsername().equals(username)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        post.update(requestDto);
        // 수정된 게시글(entity) => responseDto 변환
        PostResponseDto responseDto = new PostResponseDto(post);
        return responseDto;
    }


    // id값으로 게시글 찾는 메서드
    private Post findPost(Long id){
        return postRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
    }


    public ResponseEntity<String> deletePost(String username, Long id) {
        Post post = findPost(id);
        if (!post.getUsername().equals(username)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        postRepository.delete(post);
        // 수정된 게시글(entity) => responseDto 변환
        return new ResponseEntity<>("msg : 삭제가 완료되었습니다.", HttpStatus.OK);
    }
}
