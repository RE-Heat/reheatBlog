package com.reheat.reheatlog.request.comment;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

@Data
@ToString
@NoArgsConstructor
public class CommentCreate {
    @Length(min = 1, max = 8, message = "작성자는 1~8글자까지 입력해주세요")
    @NotBlank(message = "작성자를 입력해주세요")
    private String author;

    @Length(min = 6, max = 30, message = "비밀번호는 최소 6글자 최대 30글자까지 입력해주세요")
    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

    @Length(min = 10, max = 1000, message = "댓글은 최소 10글자 최대 500글자까지 입력할 수 있습니다")
    @NotBlank(message = "내용을 입력해주세요")
    private String content;

    @Builder
    public CommentCreate(String author, String password, String content) {
        this.author = author;
        this.password = password;
        this.content = content;
    }
}
