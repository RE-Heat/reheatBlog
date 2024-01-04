package com.reheat.reheatlog.service;

import com.reheat.reheatlog.domain.Comment;
import com.reheat.reheatlog.domain.Post;
import com.reheat.reheatlog.exception.PostNotFound;
import com.reheat.reheatlog.repository.comment.CommentRepository;
import com.reheat.reheatlog.repository.post.PostRepository;
import com.reheat.reheatlog.request.comment.CommentCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void write(Long postId, CommentCreate request) {
        //존재하는 글인지 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);

        //비밀번호 암호화
        String encryptedPassword = passwordEncoder.encode(request.getPassword());

        Comment comment = Comment.builder()
                .author(request.getAuthor())
                .password(encryptedPassword)
                .content(request.getContent())
                .build();

        post.addComment(comment);
    }
}
