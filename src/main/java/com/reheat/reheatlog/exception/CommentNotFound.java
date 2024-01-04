package com.reheat.reheatlog.exception;

public class CommentNotFound extends reheatException {
    private static final String MESSAGE = "존재하지 않는 댓글입니다";

    public CommentNotFound() {
        super(MESSAGE);
    }

    public int getStatusCode() {
        return 404;
    }
}
