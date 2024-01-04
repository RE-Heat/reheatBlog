package com.reheat.reheatlog.controller;

import com.reheat.reheatlog.config.UserPrincipal;
import com.reheat.reheatlog.request.PostCreate;
import com.reheat.reheatlog.request.PostEdit;
import com.reheat.reheatlog.response.PostResponse;
import com.reheat.reheatlog.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/posts")
    public void post(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody @Validated PostCreate request) {
        //인증
        //1. get Parameter -> ?? 이걸로 !
        //2. Post value?
        //3. Header

        postService.write(userPrincipal.getUserId(), request);
    }

    //단건 조회
    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId) {
        return postService.get(postId);
    }

    //여러 건 조회
    @GetMapping("/posts")
    public List<PostResponse> getList(Pageable pageable) {
        return postService.getList(pageable);
    }

    //게시글 수정
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/posts/{postId}")
    public PostResponse edit(@PathVariable Long postId, @RequestBody PostEdit request) {
        return postService.edit(postId, request);
    }

    //게시글 삭제
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PreAuthorize("hasRole('ROLE_ADMIN') && hasPermission(#postId, 'POST', 'DELETE')")
    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }
}
