package com.reheat.reheatlog.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class PostCreate {

    @NotBlank(message = "타이틀을 입력해주세요")
    private String title;

    @NotBlank(message = "콘텐츠를 입력해주세요")
    private String content;

    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public PostCreate changTitle(String title) {
        this.title = title;
        return PostCreate.builder()
                .title(title)
                .content(content)
                .build();
    }

    //빌더의 장점
    // - 가독성이 좋다(값 생성에 대한 유연함)
    // - 필요한 값만 받을 수 있다. // -> (오버로딩 가능한 조건)
    // - 객체의 불변성

}
