package com.reheat.reheatlog.service;

import com.reheat.reheatlog.domain.Post;
import com.reheat.reheatlog.domain.PostEditor;
import com.reheat.reheatlog.exception.PostNotFound;
import com.reheat.reheatlog.exception.UserNotFound;
import com.reheat.reheatlog.repository.UserRepository;
import com.reheat.reheatlog.repository.post.PostRepository;
import com.reheat.reheatlog.request.post.PostCreate;
import com.reheat.reheatlog.request.post.PostEdit;
import com.reheat.reheatlog.request.post.PostSearch;
import com.reheat.reheatlog.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void write(Long userId, PostCreate postCreate) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFound());

        Post post = Post.builder()
                .user(user)
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

        postRepository.save(post);

        //클라이언트 요구사항
        //json 응답에서 title 10글자로 제한
    }

    public PostResponse get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    public List<PostResponse> getList(PostSearch postSearch) {

        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostResponse edit(Long postId, PostEdit postEdit) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);

        //post.change(postEdit.getTitle(), postEdit.getContent());
        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();

        if (postEdit.getTitle() != null) {
            editorBuilder.title(postEdit.getTitle());
        }

        if (postEdit.getContent() != null) {
            editorBuilder.content(postEdit.getContent());
        }

        post.edit(editorBuilder.build());

        return new PostResponse(post);
    }

    public void delete(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);
        postRepository.delete(post);
    }
}










