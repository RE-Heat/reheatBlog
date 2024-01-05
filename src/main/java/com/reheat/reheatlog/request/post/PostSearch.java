package com.reheat.reheatlog.request.post;

import lombok.Builder;
import lombok.Setter;

@Setter
public class PostSearch {
    private static final int MAX_SIZE = 2000;

    private Integer page;
    private Integer size;

    public Integer getPage() {
        return page != null ? page : 1;
    }

    public Integer getSize() {
        return size != null ? size : 10;
    }

    public long getOffset() {
        return (long) (Math.max(1, getPage()) - 1) * Math.min(getSize(), MAX_SIZE);
    }

    @Builder
    public PostSearch(Integer page, Integer size) {
        this.page = page;
        this.size = size;
    }
}
