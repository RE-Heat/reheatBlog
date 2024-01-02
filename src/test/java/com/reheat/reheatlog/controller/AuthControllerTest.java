package com.reheat.reheatlog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reheat.reheatlog.domain.Session;
import com.reheat.reheatlog.domain.User;
import com.reheat.reheatlog.repository.SessionRepository;
import com.reheat.reheatlog.repository.UserRepository;
import com.reheat.reheatlog.request.Login;
import com.reheat.reheatlog.request.Signup;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 성공")
    void test1() throws Exception {
        // given
        User user = User.builder()
                .email("reheat1234@gmail.com")
                .password("1234")
                .build();

        userRepository.save(user);

        Login login = Login.builder()
                .email("reheat1234@gmail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        // expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Transactional
    @DisplayName("로그인 성공 후 세션 한 개 생성")
    void test2() throws Exception {
        // given
        User user = User.builder()
                .email("reheat1234@gmail.com")
                .password("1234")
                .build();

        userRepository.save(user);

        Login login = Login.builder()
                .email("reheat1234@gmail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        // expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        User loggedInUser = userRepository.findById(user.getId())
                .orElseThrow(RuntimeException::new);

        assertEquals(1L, loggedInUser.getSessions().size());
    }

    @Test
    @Transactional
    @DisplayName("로그인 성공 후 세션 응답")
    void test3() throws Exception {
        // given
        User user = User.builder()
                .email("reheat1234@gmail.com")
                .password("1234")
                .build();

        userRepository.save(user);

        Login login = Login.builder()
                .email("reheat1234@gmail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        // expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", Matchers.notNullValue()))
                //.andExpect(header().exists("accessToken"))
                .andDo(print());
    }

    @Test
    @Transactional
    @DisplayName("로그인 성공 후 권한이 필요한 페이지 접속 /foo")
    void test4() throws Exception {
        // given
        User user = User.builder()
                .name("리히트")
                .email("reheat1234@gmail.com")
                .password("1234")
                .build();
        Session session = user.addSession();
        userRepository.save(user);


        // expected
        mockMvc.perform(get("/foo")
                        .header("Authorization", session.getAccessToken())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Transactional
    @DisplayName("로그인 후 검증되지 않은 세션값으로 권한이 필요한 페이지에 접속할 수 없다")
    void test5() throws Exception {
        // given
        User user = User.builder()
                .name("리히트")
                .email("reheat1234@gmail.com")
                .password("1234")
                .build();
        Session session = user.addSession();
        userRepository.save(user);


        // expected
        mockMvc.perform(get("/foo")
                        .header("Authorization", session.getAccessToken() + "-other")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }

    @Test
    @DisplayName("회원가입 성공")
    void test6() throws Exception {
        //given
        Signup signup = Signup.builder()
                .email("reheat1540@gmail.com")
                .password("1234")
                .name("리히트")
                .build();

        //expected
        String json = objectMapper.writeValueAsString(signup);

        mockMvc.perform(post("/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
    }
}