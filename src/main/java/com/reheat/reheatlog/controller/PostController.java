package com.reheat.reheatlog.controller;

import com.reheat.reheatlog.request.PostCreate;
import com.reheat.reheatlog.response.PostResponse;
import com.reheat.reheatlog.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

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

    @GetMapping("/posts")
    public String get() {
        return "Hello World";
    }

    @PostMapping("/posts")
    public void post(@RequestBody @Validated PostCreate request) {
        //Case1 저장한 데이터 Entity -> response로 응답하기
        //Case2 저장한 데이터의 id만 리턴
        //      클라이언트에선 수신한 id를 글 조회 API를 통해서 글 데이터를 수신받음.
        //Case3. 응답 필요 없음 -> 클라이언트에서 모든 POST 데이터 context를 관리함
        //Bad Case : 서버에서 반드시 이렇게 할 겁니다 fix X
        //          -> 서버에서 유연하게 대응하는 게 좋다.
        //          -> 한 번에 일괄적으로 잘 처리되는 케이스가 없다. 잘 관리하는 형태가 중요하다.

        postService.write(request);
    }

    /**
     * /posts-> 글 전체 조회(검색 + 페이징)
     * /posts/{postId} -> 글 단건 조회
     */

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId) {
        PostResponse response = postService.get(postId);
        return response;
    }

}
