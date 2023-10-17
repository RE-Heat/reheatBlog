package com.reheat.reheatlog.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("/posts 요청시 Hello World 출력")
    public void test() throws Exception {
        //글 제목
        //글 내용
        //사용자
            // id
            // name
            // level

        /**
         * {
         *     "title" : "xxx",
         *     "content" : "XXX",
         *     "user"
         *
         * }
         */

        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"제목입니다.\", \"content\": \"내용입니다.\"}")
                        .param("title", "글 제목입니다")
                        .param("content", "글 내용입니다")
                ) //MockMVC content-Type은 application/json
                .andExpect(status().isOk())
                .andExpect(content().string("Hello World"))
                .andDo(print())
        ;
    }

    @Test
    @DisplayName("/posts 요청시 title값은 필수다.")
    public void test2() throws Exception {

        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": null, \"content\": \"내용입니다.\"}")
                ) //MockMVC content-Type은 application/json
                .andExpect(status().isBadRequest())

                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다"))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요"))
                .andDo(print());
    }
}