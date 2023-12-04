package com.reheat.reheatlog.controller;

import com.reheat.reheatlog.request.PostCreate;
import com.reheat.reheatlog.request.PostEdit;
import com.reheat.reheatlog.response.PostResponse;
import com.reheat.reheatlog.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/posts")
    public void post(@RequestBody @Validated PostCreate request) {
        //Case1 저장한 데이터 Entity -> response로 응답하기
        //Case2 저장한 데이터의 id만 리턴
        //      클라이언트에선 수신한 id를 글 조회 API를 통해서 글 데이터를 수신받음.
        //Case3. 응답 필요 없음 -> 클라이언트에서 모든 POST 데이터 context를 관리함
        //Bad Case : 서버에서 반드시 이렇게 할 겁니다 fix X
        //          -> 서버에서 유연하게 대응하는 게 좋다.
        //          -> 한 번에 일괄적으로 잘 처리되는 케이스가 없다. 잘 관리하는 형태가 중요하다.

        request.validate();

        //부적절한 언어 제외

        postService.write(request);
    }

    //단건 조회
    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId) {
        return postService.get(postId);
    }

    //여러 건 조회
    @GetMapping("/posts")
    public List<PostResponse> getList(Pageable pageable){
        return postService.getList(pageable);
    }

    //게시글 수정
    @PatchMapping("/posts/{postId}")
    public PostResponse edit(@PathVariable Long postId, @RequestBody PostEdit request){
        return postService.edit(postId, request);
    }

    //게시글 삭제
    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId){
        postService.delete(postId);
    }
}
