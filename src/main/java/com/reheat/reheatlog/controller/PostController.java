package com.reheat.reheatlog.controller;

import com.reheat.reheatlog.request.PostCreate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class PostController {
    //GET, POST, PUT, PATCH, DELETE, OPTIONS, HEAD, TRACE, CONNECT
    //글 등록은 POST
//    @PostMapping("/posts")
//    public String post(@RequestParam String title, @RequestParam String content) {
//        log.info("title={}, content={}", title, content);
//        return "post 성공!";
//    }

//    @PostMapping("/posts")
//    public String post(@RequestParam Map<String, String> params) {
//        return "post 성공!";
//    }

    @PostMapping("/posts")
    public Map<String, String> post(@RequestBody @Validated PostCreate params) {
        return Map.of();
    }


    @GetMapping("/posts")
    public String get() {
        return "Hello World";
    }
}
