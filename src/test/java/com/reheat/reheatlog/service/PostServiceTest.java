package com.reheat.reheatlog.service;

import com.reheat.reheatlog.domain.Post;
import com.reheat.reheatlog.domain.User;
import com.reheat.reheatlog.exception.PostNotFound;
import com.reheat.reheatlog.repository.UserRepository;
import com.reheat.reheatlog.repository.post.PostRepository;
import com.reheat.reheatlog.request.post.PostCreate;
import com.reheat.reheatlog.request.post.PostEdit;
import com.reheat.reheatlog.request.post.PostSearch;
import com.reheat.reheatlog.response.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {
    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1() throws Exception {
        //given
        var user = User.builder()
                .name("리히트")
                .email("reheat1540@gmail.com")
                .password("1234")
                .build();

        userRepository.save(user);

        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다")
                .content("내용입니다")
                .build();

        //when
        postService.write(user.getId(), postCreate);

        //then
        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다", post.getTitle());
        assertEquals("내용입니다", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test2() throws Exception {
        //given

        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(requestPost);

        //when
        PostResponse post = postService.get(requestPost.getId());

        //then
        assertNotNull(post);
        assertEquals(1L, postRepository.count());
        assertEquals("foo", post.getTitle());
        assertEquals("bar", post.getContent());
    }

    @Test
    @DisplayName("글 여러 개 조회")
    void test3() throws Exception {
        //given
        postRepository.saveAll(List.of(
                Post.builder()
                        .title("foo")
                        .content("bar")
                        .build(),
                Post.builder()
                        .title("foo")
                        .content("bar")
                        .build()
        ));
        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .size(10)
                .build();
        //when
        List<PostResponse> posts = postService.getList(postSearch);

        //then
        assertEquals(2L, postRepository.count());
    }

    @Test
    @DisplayName("글 1페이지 조회")
    public void test4() throws Exception {
        //given
        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> Post.builder()
                        .title("제목 " + i)
                        .content("푸르지오 " + i)
                        .build())
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .build();

        //when
        List<PostResponse> posts = postService.getList(postSearch);

        //then
        assertEquals(10L, posts.size()); //페이지 처리됐으므로 10개
        assertEquals("제목 19", posts.get(0).getTitle()); //가장 마지막. id desc니까
    }

    @Test
    @DisplayName("글 제목 수정")
    public void test5() throws Exception {
        //given
        Post post1 = Post.builder()
                .title("야호호")
                .content("푸르지오")
                .build();

        postRepository.save(post1);

        //클라이언트가 수정할 값 안 보내면.
        PostEdit postEdit = PostEdit.builder()
                .title("갓영한")
                .build();

        //when
        postService.edit(post1.getId(), postEdit);

        //then
        Post changePost = postRepository.findById(post1.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id =" + post1.getId()));
        assertEquals("갓영한", changePost.getTitle());
    }

    @Test
    @DisplayName("글 내용 수정")
    public void test6() throws Exception {
        //given
        Post post1 = Post.builder()
                .title("야호호")
                .content("푸르지오")
                .build();

        postRepository.save(post1);

        //클라이언트가 수정할 값 안 보내면.
        PostEdit postEdit = PostEdit.builder()
                .title("야호호")
                .content("반포자이")
                .build();

        //when
        postService.edit(post1.getId(), postEdit);

        //then
        Post changePost = postRepository.findById(post1.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id =" + post1.getId()));
        assertEquals("반포자이", changePost.getContent());
    }

    @Test
    @DisplayName("게시글 삭제")
    void test7() throws Exception {
        //given
        Post post = Post.builder()
                .title("야호호")
                .content("푸르지오")
                .build();

        postRepository.save(post);

        //when
        postService.delete(post.getId());

        //then
        assertEquals(0, postRepository.count());
    }

    @Test
    @DisplayName("글 1개 조회")
        //실패 사례
    void test8() throws Exception {
        //given

        Post post = Post.builder()
                .title("리히트")
                .content("푸르지오")
                .build();
        postRepository.save(post);

        //expected
        PostNotFound postNotFound = assertThrows(PostNotFound.class, () -> {
            postService.get(post.getId() + 1L);
        });

        assertEquals("존재하지 않는 글입니다", postNotFound.getMessage());
    }

    @Test
    @DisplayName("게시글 삭제 - 예외 케이스")
    void test9() throws Exception {
        //given
        Post post = Post.builder()
                .title("야호호")
                .content("푸르지오")
                .build();

        postRepository.save(post);

        //expected
        assertThrows(PostNotFound.class, () -> {
            postService.delete(post.getId() + 1L);
        });
    }


    @Test
    @DisplayName("글 내용 수정 - 예외케이스")
    public void test10() throws Exception {
        //given
        Post post = Post.builder()
                .title("야호호")
                .content("푸르지오")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("야호호")
                .content("반포자이")
                .build();

        //expected
        assertThrows(PostNotFound.class, () -> {
            postService.edit(post.getId() + 1L, postEdit);
        });
    }
}

