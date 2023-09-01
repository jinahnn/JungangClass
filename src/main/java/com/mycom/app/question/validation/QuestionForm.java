package com.mycom.app.question.validation;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

//질문등록폼(question_form.html)의 입력값에 대한 유효성검사
/*
    @NotNull    null금지      / ""과 " "허용
    @NotEmpty   null과 ""금지 / " "허용
    @NotBlank   null과 ""과 " "금지            */

@Getter
@Setter
public class QuestionForm {
    //제목
    @NotEmpty(message = "제목은 필수입력해야합니다.")
    @Size(max=200) //Queestion Entitu에서 @column(length=200)에 맞추어
     private String subject; //제목

    //내용
    //제목
    @NotEmpty(message = "내용은 필수입력해야합니다.")
    private String content; //제목
}
