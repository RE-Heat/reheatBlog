package com.reheat.reheatlog.exception;

public class UserNotFound extends reheatException {
    private static final String MESSAGE = "존재하지 않는 유저입니다";

    public UserNotFound() {
        super(MESSAGE);
    }

    public int getStatusCode() {
        return 404;
    }
}
