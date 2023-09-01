package com.mycom.app.answer.controller;

import com.mycom.app.answer.entity.Answer;
import com.mycom.app.answer.service.AnswerService;
import com.mycom.app.answer.validtion.AnswerForm;
import com.mycom.app.question.entity.Question;
import com.mycom.app.question.service.QuestionService;
import com.mycom.app.question.validation.QuestionForm;
import com.mycom.app.user.entity.SiteUser;
import com.mycom.app.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;

    //딥변삭제
    /*요청주소  /answer/delete/답변번호${answer.id}
      요청방식  post */
    @GetMapping("/delete/{id}")
    public String answerDelete(@PathVariable("id") Integer id,
                               Principal principal){
        //1.파라미터받기
        //2.비즈니스로직수행
        Answer answer = answerService.getAnswer(id); //답변상세가져오기
        if ( !answer.getWriter().getUsername().equals(principal.getName()) ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"삭제권한이 없습니다.");
        }
        answerService.delete(answer);
        return "redirect:/question/detail/"+answer.getQuestion().getId(); //(질문상세조회요청에 따른)질문상세페이지로 이동
    }

    //답변수정폼을 보여줘  /answer/modify/${answer.id} get방식
    @PreAuthorize("isAuthenticated()") //인증을 요하는 메서드
    @GetMapping("/modify/{id}")
    public String answerModify(AnswerForm answerForm,
                               @PathVariable("id") Integer id, Principal principal){
        //1.파라미터받기
        //2.비즈니스로직수행
        Answer answer = answerService.getAnswer(id);//답변상세
        if ( !answer.getWriter().getUsername().equals(principal.getName()) ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한이 없습니다.");
        }
        answerForm.setContent(answer.getContent());
        //3.Model  //4.View
        return "answer_form"; //답변저장폼으로 이동
    }

    //답변수정처리  /answer/modify/${answer.id} post방식
    @PostMapping("/modify/{id}")
    public String modify(@Valid AnswerForm answerForm,BindingResult bindingResult,
                         @PathVariable("id") Integer id,Principal principal){
        //1.파라미터받기
        if(bindingResult.hasErrors()){  //유효성검사시 에러가 발생하면
            return "answer_form"; //answer_form.html문서로 이동
        }
        //2.비즈니스로직수행
        //로그인한 유저가 글쓴이와 일치해야지만 수정권한을 가지게 된다=>수정처리 진행됨
        Answer answer = answerService.getAnswer(id); //답변상세
        if ( !answer.getWriter().getUsername().equals(principal.getName()) ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한이 없습니다.");
        }

        answerService.moddify(answer,answerForm.getContent());
        /*답변이 달렸던 질문의 상세페이지로 이동하기 위해서
        질문상세조회요청주소는 /question/detail/질문번호이다
        따라서 리다이렉트주소는 질문번호를 가져오는 answer.getQuestion().getId()를 이용해야하므로
        아래와 같이 작성해야 한다*/
        return String.format("redirect:/question/detail/%d",answer.getQuestion().getId()); //질문상세페이지로 이동

    }


    /*요청방식 post
     요청주소  /answer/add/10질문번호   */
    //답변등록처리
    @PreAuthorize("isAuthenticated()")//로그인인증=>로그인이 필요한 기능
    @PostMapping("/add/{id}")
    //유효성검사관련  @Valid AnswerForm  answerForm)
    //스프링시큐리티가 제공하는 객체. 사용자정보. Principal
    public String addAnswer(@PathVariable("id") Integer id, Model model,
                            @Valid AnswerForm answerForm, BindingResult bindingResult,
                            Principal principal) {
        //1.파라미터받기
        //2.비즈니스로직
        //특정글번호의 질문상세가져오기 =>Answer테이블의 fk에 해당하는 질문객체
        /* Answer 엔티티의 속성으로  @ManyToOne    private Question question; 존재*/
        Question question = questionService.getQuestion(id);
        if(bindingResult.hasErrors()){  //유효성검사를 통과하지못하여 에러가 존재하면
            model.addAttribute("question",question);
            return "question_detail";  //question_detail.html로 이동
        }
        SiteUser siteUser = userService.getUser(principal.getName());//user정보가져오기
        System.out.println("대답컨트롤러 siteUser ="+siteUser);
        answerService.add(question, answerForm.getContent(), siteUser);
        //3.Model
        //4.View
        //return "q.html";
        return String.format("redirect:/question/detail/%d",id); //question_detail.html문서
    }

    //답변추천
    //요청주소    /answer/vote/답변번호 ${answer.id}
    //요청방식    get
    @PreAuthorize("isAuthenticated()")//로그인인증=>로그인이 필요한 기능
    @GetMapping("/vote/{id}")
    public String questionVote(@PathVariable("id") Integer id,Principal principal){
        Question question = questionService.getQuestion(id); //해당질문의 상세정보조회
        //principal.getName() //로그인한 유저
        //로그인한 유저를 이용하여  site_user테이블에서  유저의 상세정보를 가져오기=>새 추천인정보
        SiteUser siteUser= userService.getUser(principal.getName());    //추천인정보
        questionService.vote(question,siteUser);

        //질문상세요청은/question/detail/질문번호 이므로
        //위에서 받은 파라미터인 답변번호를 통해 해달질문이 몇번인지. 즉 질문번호룰 구하여
        //질문상세 요청을 완성해야한다.
        return String.format("redirect:/question/detail/%d",id);//(질문상세조회요청을 통한)질문상세페이지로 이동
    }

}









