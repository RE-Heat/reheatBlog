package com.reheat.reheatlog.controller;

import com.reheat.reheatlog.request.PostCreate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    public String post(@RequestBody PostCreate params) {
        log.info("params={}", params.toString());
        return "Hello World";
    }


    @GetMapping("/posts")
    public String get() {
        return "Hello World";
    }
}
