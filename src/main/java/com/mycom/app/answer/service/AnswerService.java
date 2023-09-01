package com.mycom.app.answer.service;

import com.mycom.app.answer.entity.Answer;
import com.mycom.app.answer.repository.AnswerRepository;
import com.mycom.app.exception.DataNotFoundException;
import com.mycom.app.question.entity.Question;
import com.mycom.app.user.entity.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AnswerService {

    private final AnswerRepository answerRepository;

    //답변삭제
    public void delete(Answer answer) {
        answerRepository.delete(answer);
    }

    //답변수정
    public void moddify(Answer answer,String content){
        answer.setContent(content);
        answer.setModifyDate(LocalDateTime.now());//답변수정일
        answerRepository.save(answer);
    }

    //답변상세조회
    public Answer getAnswer(Integer id) {
        Optional<Answer> answer = answerRepository.findById(id);
        if(answer.isPresent()){
            return answer.get();
        }else{
            throw new DataNotFoundException("Answer not Found");
        }
    }


    //답변등록처리
    //SiteUser siteUser : 답변작성하는 user정보
    public void add(Question question, String content, SiteUser asiteUser){
        Answer answer = new Answer();
        answer.setContent(content);
        answer.setCreateDate(LocalDateTime.now());
        answer.setQuestion(question); //fk에 해당하는 질문객체
        answer.setWriter(asiteUser);
        System.out.println("질문서비스 answer="+answer);
        answerRepository.save(answer);
    }
 //답변추천
    public void vote(Answer answer, SiteUser siteUser){
        //answer.getVoter() : 기존추천목록을 가져온다=> Set<SiteUser>
        //Set참조변수명.add(값) : Set인터페이스에 값을 추가
        //기존추천목록.add(새로운 추천인);
        answer.getVoter().add(siteUser);
        answerRepository.save(answer);
    }

}
