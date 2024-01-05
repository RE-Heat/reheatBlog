package com.reheat.reheatlog.repository.post;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.reheat.reheatlog.domain.Post;
import com.reheat.reheatlog.domain.QPost;
import com.reheat.reheatlog.request.post.PostSearch;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getList(PostSearch postSearch) {
        return jpaQueryFactory.selectFrom(QPost.post)
                .limit(postSearch.getSize())
                .offset(postSearch.getOffset())
                .orderBy(QPost.post.id.desc())
                .fetch();
    }
}
