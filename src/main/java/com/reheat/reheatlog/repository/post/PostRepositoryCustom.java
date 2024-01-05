package com.reheat.reheatlog.repository.post;

import com.reheat.reheatlog.domain.Post;
import com.reheat.reheatlog.request.post.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
