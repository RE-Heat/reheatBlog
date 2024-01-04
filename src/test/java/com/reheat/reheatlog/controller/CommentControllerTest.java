package com.reheat.reheatlog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reheat.reheatlog.domain.Comment;
import com.reheat.reheatlog.domain.Post;
import com.reheat.reheatlog.domain.User;
import com.reheat.reheatlog.repository.UserRepository;
import com.reheat.reheatlog.repository.comment.CommentRepository;
import com.reheat.reheatlog.repository.post.PostRepository;
import com.reheat.reheatlog.request.comment.CommentCreate;
import com.reheat.reheatlog.request.comment.CommentDelete;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class CommentControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void clean() {
        commentRepository.deleteAll();
        userRepository.deleteAll();
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("댓글 작성")
    void test1() throws Exception {
        //given
        User user = userRepository.findAll().get(0);

        userRepository.save(user);

        Post post = Post.builder()
                .title("야호호")
                .content("푸르지오")
                .user(user)
                .build();
        postRepository.save(post);

        CommentCreate request = CommentCreate.builder()
                .author("게스트1")
                .password("123456")
                .content("댓글1234567890")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //when
        mockMvc.perform(post("/posts/{postId}/comments", post.getId())
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(json)
                )
                .andDo(print())
                .andExpect(status().isOk());

        //then
        assertEquals(1L, commentRepository.count());
        Comment savedComment = commentRepository.findAll().get(0);
        assertEquals("게스트1", savedComment.getAuthor());
        assertNotEquals("123456", savedComment.getPassword());
        assertTrue(passwordEncoder.matches("123456", savedComment.getPassword()));
        assertEquals("댓글1234567890", savedComment.getContent());

    }

    @Test
    @DisplayName("댓글 삭제")
    void test2() throws Exception {
        //given
        User user = userRepository.findAll().get(0);

        userRepository.save(user);

        Post post = Post.builder()
                .title("야호호")
                .content("푸르지오")
                .user(user)
                .build();
        postRepository.save(post);

        String encryptedPassword = passwordEncoder.encode("123456");

        Comment comment = Comment.builder()
                .author("게스트1")
                .password(encryptedPassword)
                .content("댓글1234567890")
                .build();
        comment.setPost(post);
        commentRepository.save(comment);

        CommentDelete request = CommentDelete.builder()
                .password("123456")
                .build();
        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/comments/{commentId}/delete", comment.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }
}