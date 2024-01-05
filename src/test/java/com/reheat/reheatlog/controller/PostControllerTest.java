package com.reheat.reheatlog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reheat.reheatlog.annotation.ReheatlogMockUser;
import com.reheat.reheatlog.domain.Post;
import com.reheat.reheatlog.domain.User;
import com.reheat.reheatlog.repository.UserRepository;
import com.reheat.reheatlog.repository.post.PostRepository;
import com.reheat.reheatlog.request.post.PostCreate;
import com.reheat.reheatlog.request.post.PostEdit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void clean() {
        userRepository.deleteAll();
        postRepository.deleteAll();
    }


    //    @Test
//    @DisplayName("/posts 요청시 title값은 필수다.")
    void test2() throws Exception {
        // given
        PostCreate request = PostCreate.builder()
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력하세요."))
                .andDo(print());
    }

    @Test
    @ReheatlogMockUser
    @DisplayName("글 작성")
    void test3() throws Exception {

        //given
        PostCreate request = PostCreate.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());
        //then
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다", post.getTitle());
        assertEquals("내용입니다", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test4() throws Exception {
        //given
        User user = User.builder()
                .name("게스트1")
                .password("1234")
                .email("guest1234@gmail.com")
                .build();

        userRepository.save(user);

        Post post = Post.builder()
                .title("1234214123213")
                .content("bar")
                .user(user)
                .build();

        //클라이언트 요구사항
        //json 응답에서 title 10글자로 제한

        postRepository.save(post);

        //expected
        mockMvc.perform(get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("1234214123"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andDo(print());

    }

    @Test
    @DisplayName("글 여러 건 조회")
    void test5() throws Exception {
        //given
        User user = User.builder()
                .name("게스트1")
                .password("1234")
                .email("guest1234@gmail.com")
                .build();

        userRepository.save(user);

        Post post1 = postRepository.save(Post.builder()
                .title("title_1")
                .content("content_1")
                .user(user)
                .build());

        Post post2 = postRepository.save(Post.builder()
                .title("title_2")
                .content("content_2")
                .user(user)
                .build());


        //expected
        mockMvc.perform(get("/posts")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].id").value(post2.getId()))
                .andExpect(jsonPath("$[0].title").value("title_2"))
                .andExpect(jsonPath("$[0].content").value("content_2"))
                .andExpect(jsonPath("$[1].id").value(post1.getId()))
                .andExpect(jsonPath("$[1].title").value("title_1"))
                .andExpect(jsonPath("$[1].content").value("content_1"))
                .andDo(print());
    }

    @Test
    @DisplayName("페이지를 0으로 요청하면 첫 페이지를 가져온다")
    void test6() throws Exception {
        //given
        User user = User.builder()
                .name("게스트1")
                .password("1234")
                .email("guest1234@gmail.com")
                .build();

        userRepository.save(user);

        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> Post.builder()
                        .title("제목 " + i)
                        .content("푸르지오 " + i + "동")
                        .user(user)
                        .build()
                ).collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        //expected
        mockMvc.perform(get("/posts?page=0&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
//                .andExpect(jsonPath("$[0].id").value(36))
                .andExpect(jsonPath("$[0].title").value("제목 19"))
                .andExpect(jsonPath("$[0].content").value("푸르지오 19동"))
                .andDo(print());
    }

    @Test
    @ReheatlogMockUser
    @DisplayName("글 제목 수정")
    public void test7() throws Exception {
        //given
        User user = userRepository.findAll().get(0);

        userRepository.save(user);

        Post post = Post.builder()
                .title("야호호")
                .content("푸르지오")
                .user(user)
                .build();
        postRepository.save(post);

        //클라이언트가 수정할 값 안 보내면.
        PostEdit postEdit = PostEdit.builder()
                .title(null)
                .content("푸르지오")
                .build();


        //expected
        mockMvc.perform(patch("/posts/{postId}", post.getId()) //PATCH /posts/{postId}
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @ReheatlogMockUser
    @DisplayName("게시글 삭제")
    void test8() throws Exception {
        //given
        User user = userRepository.findAll().get(0);

        userRepository.save(user);

        Post post = Post.builder()
                .title("야호호")
                .content("푸르지오")
                .user(user)
                .build();
        postRepository.save(post);


        //expected
        mockMvc.perform(delete("/posts/{postId}", post.getId()) //PATCH /posts/{postId}
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 글 조회")
    void test9() throws Exception {
        //given

        //expected
        mockMvc.perform(get("/posts/{postId}", 1L) //DELETE /posts/{postId}
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @WithMockUser(username = "reheat1540@gmail.com", roles = {"ADMIN"})
    @DisplayName("존재하지 않은 게시글 수정")
    void test10() throws Exception {
        //given
        PostEdit postEdit = PostEdit.builder()
                .title(null)
                .content("푸르지오")
                .build();

        //expected
        mockMvc.perform(patch("/posts/{postId}", 1L) //PATCH /posts/{postId}
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}

// API 문서 생성
// GET /posts/{postId} -> 단건 조회
// POST

// Spring RestDocs
// - 운영 코드에 -> 영향
// - 코드 수정 -> 문서 수정 X -> 문서 신뢰성 떨어짐
//그런데 Spring RestDocs는 Test 케이스를 실행 후 문서를 생성해준다.