package com.reheat.reheatlog.request.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
public class CommentDelete {

    @Length(min = 6, max = 30, message = "비밀번호는 최소 6글자 최대 30글자까지 입력해주세요")
    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

    @Builder
    public CommentDelete(String password) {
        this.password = password;
    }
}
