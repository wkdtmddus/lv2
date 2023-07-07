package com.sparta.lv2.repository;

import com.sparta.lv2.dto.PostResponseDto;
import com.sparta.lv2.entity.Post;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();
}
