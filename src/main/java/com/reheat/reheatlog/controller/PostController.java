package com.reheat.reheatlog.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostController {
    //GET, POST, PUT, PATCH, DELETE, OPTIONS, HEAD, TRACE, CONNECT

    //글 등록은 POST
    @PostMapping("/posts")
    public String post(){
        return "post 성공!";
    }

    @GetMapping("/posts")
    public String get(){
        return "Hello World";
    }
}
