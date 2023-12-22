package com.reheat.reheatlog.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Login {
    @NotBlank(message = "이메일을 입력해주세요")
    @Email(message = "이메일 형식에 맞춰주세요")
    private String email;

    @NotBlank(message = "패스워드를 입력해주세요")
    private String password;

    @Builder
    public Login(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
