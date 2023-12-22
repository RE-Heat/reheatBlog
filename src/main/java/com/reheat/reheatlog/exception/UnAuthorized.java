package com.reheat.reheatlog.exception;

public class UnAuthorized extends reheatException{
    private static final String MESSAGE = "인증이 필요합니다";

    public UnAuthorized() {
        super(MESSAGE);
    }

    public int getStatusCode(){
        return 401;
    }
}
