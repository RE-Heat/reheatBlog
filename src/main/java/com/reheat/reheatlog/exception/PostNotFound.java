package com.reheat.reheatlog.exception;

public class PostNotFound extends reheatException {
    private static final String MESSAGE = "존재하지 않는 글입니다";

    public PostNotFound() {
        super(MESSAGE);
    }

    public int getStatusCode(){
        return 404;
    }
}
