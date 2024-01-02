package com.reheat.reheatlog.service;

import com.reheat.reheatlog.crypto.ScryptPasswordEncoder;
import com.reheat.reheatlog.domain.User;
import com.reheat.reheatlog.exception.AlreadyExistsEmailException;
import com.reheat.reheatlog.exception.InvalidSigninInformation;
import com.reheat.reheatlog.repository.UserRepository;
import com.reheat.reheatlog.request.Login;
import com.reheat.reheatlog.request.Signup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class AuthServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void test() throws Exception {
        //given
        Signup signup = Signup.builder()
                .email("reheat1540@gmail.com")
                .password("1234")
                .name("리히트")
                .build();

        //when
        authService.signup(signup);
        //then
        assertEquals(1, userRepository.count());

        User user = userRepository.findAll().iterator().next();
        assertEquals("리히트", user.getName());
        assertEquals("reheat1540@gmail.com", user.getEmail());
//        assertNotNull(user.getPassword());
        assertEquals("1234", user.getPassword());

    }

    @Test
    @DisplayName("회원가입 시 중복된 이메일")
    void test2() throws Exception {
        //given
        User user = User.builder()
                .email("reheat1540@gmail.com")
                .password("1234")
                .name("리힛")
                .build();

        userRepository.save(user);

        Signup signup = Signup.builder()
                .email("reheat1540@gmail.com")
                .password("1234")
                .name("리히트")
                .build();

        //expected
        assertThrows(AlreadyExistsEmailException.class, () -> {
            authService.signup(signup);
        });
    }

    @Test
    @DisplayName("로그인 성공")
    void test3() throws Exception {
        //given
        User user = User.builder()
                .email("reheat1540@gmail.com")
                .password("1234")
                .name("리힛")
                .build();
        userRepository.save(user);

        //when
        Login login = Login.builder()
                .email("reheat1540@gmail.com")
                .password("1234")
                .build();
        Long userId = authService.signIn(login);


        //then
        assertNotNull(userId);
    }

    @Test
    @DisplayName("로그인 비밀번호 틀림")
    void test4() throws Exception {
        //given
        ScryptPasswordEncoder encoder = new ScryptPasswordEncoder();
        String encryptedPassword = encoder.encrypt("1234");

        User user = User.builder()
                .email("reheat1540@gmail.com")
                .password(encryptedPassword)
                .name("리힛")
                .build();
        userRepository.save(user);

        //when
        Login login = Login.builder()
                .email("reheat1540@gmail.com")
                .password("5678")
                .build();

        //then
        assertThrows(InvalidSigninInformation.class,
                () -> authService.signIn(login));
    }
}