package com.mycom.app.question.DTO;

import com.mycom.app.answer.entity.Answer;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter

public class QuestionDTO {
    private  Integer id;//글번호. 1씩증가.pk

    private String subject;//제목

    private String content;//내용

    private LocalDateTime createDate;//작성일

    private List<Answer> answerList;
}
