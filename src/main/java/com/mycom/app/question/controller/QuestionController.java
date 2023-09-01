package com.mycom.app.question.controller;

import com.mycom.app.answer.validtion.AnswerForm;
import com.mycom.app.question.entity.Question;
import com.mycom.app.question.repository.QuestionRepository;
import com.mycom.app.question.service.QuestionService;
import com.mycom.app.question.validation.QuestionForm;
import com.mycom.app.user.entity.SiteUser;
import com.mycom.app.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;

@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {

    private final QuestionService questionService;
    private final UserService userService;

    //삭제처리
    /*요청주소  /question/delete/${question.id}
      요청방식  post */
    @GetMapping("/delete/{id}")
    public String questionDelete(@PathVariable("id") Integer id,
                                 Principal principal){
        //1.파라미터받기
        //2.비즈니스로직수행
        Question question = questionService.getQuestion(id); //질문상세
        if ( !question.getWriter().getUsername().equals(principal.getName()) ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"삭제권한이 없습니다.");
        }
        questionService.delete(question);
        return "redirect:/question/list"; //(질문목록조회요청에 따른)질의목록페이지로 이동
    }


    //질문수정폼 보여줘=>여기에서는 질문등록폼을 이용
    /*요청주소 /question/modify/${question.id}
    요청방식 get     */
    @PreAuthorize("isAuthenticated()") //인증을 요하는 메서드
    @GetMapping("/modify/{id}")
    public String qeuestionModify(QuestionForm questionForm,
                                  @PathVariable("id") Integer id,Principal principal){
        //1.파라미터받기
        //2.비즈니스로직수행
        Question question = questionService.getQuestion(id); //질문상세
        if ( !question.getWriter().getUsername().equals(principal.getName()) ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한이 없습니다.");
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent(question.getContent());
        //3.Model  //4.View
        return "question_form"; //질문등록폼으로 이동
    }

    /*질문수정처리
    /*요청주소 /question/modify/${question.id}
    요청방식 post     */
    @PostMapping("/modify/{id}")
    public String modify(@Valid QuestionForm questionForm,BindingResult bindingResult,
                         @PathVariable("id") Integer id,Principal principal){
        //1.파라미터받기
        if(bindingResult.hasErrors()){  //유효성검사시 에러가 발생하면
            return "question_form"; //question_form.html문서로 이동
        }
        //2.비즈니스로직수행
        //로그인한 유저가 글쓴이와 일치해야지만 수정권한을 가지게 된다=>수정처리 진행됨
        Question question = questionService.getQuestion(id); //질문상세
        if ( !question.getWriter().getUsername().equals(principal.getName()) ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한이 없습니다.");
        }

        questionService.modify(question,questionForm.getSubject(), questionForm.getContent());
        return String.format("redirect:/question/detail/%d",id); //수정상세페이지로 이동
    }


    /*질문등록폼 보여줘 요청
    요청방식  get
    요청주소  http://localhost:포트/question/add
    모델
    view    templates/question_form.html*/
    @PreAuthorize("isAuthenticated()")//로그인인증=>로그인이 필요한 기능
    @GetMapping("/add")
    public String add(QuestionForm questionForm){
        return "question_form";
    }

    /*질문등록처리 요청
    요청방식  post
    요청주소  http://localhost:포트/question/add
    모델
    view    templates/question_list.html (질문목록조회요청을 통한)질문목록페이지로 이동*/
    @PreAuthorize("isAuthenticated()")//로그인인증=>로그인이 필요한 기능
    @PostMapping("/add")
    //public String questionAdd(@RequestParam String subject,@RequestParam String content) {
    /*파라미터
    @Valid QuestionForm questionForm - 유효성검사를 거친 QuestionForm객체타입으로 유저가 입력한 값을 받았다
    @Valid을 적용하면  QuestionForm클래스의 @NotEmpty,@Size이 적용된다
    BindingResult bindingResult - @Valid이 적용된 결과(유효하다고 검증된 데이터)를 의미
                                  @Valid가 붙은 파라미터 뒤에 작성한다.
    */
    public String questionAdd(@Valid QuestionForm questionForm, BindingResult bindingResult,
                              Principal principal) {
        //1.파라미터받기  subject=제목, content=내용
        //2.비즈니스로직
        if(bindingResult.hasErrors()) { //에러가 존재하면
            return "question_form"; //question_form.html문서로 이동
        }
        SiteUser siteUser = userService.getUser(principal.getName());//user정보가져오기
        System.out.println("질문컨트롤러 siteUser ="+siteUser);
        //questionForm.getSubject() : (제시한 유효성검사를 통과한 데이터)폼에서 subject필드값가져오기
        //questionForm.getContent() : (제시한 유효성검사를 통과한 데이터)폼에서 content필드값가져오기
        questionService.add(questionForm.getSubject(), questionForm.getContent(), siteUser);
        //3.Model
        //4.View
        return "redirect:/question/list"; //(질문목록조회요청을 통한)질문목록페이지로 이동
    }

    /*질문상세조회
    요청방식  get
    요청주소  http://localhost:포트/question/detail/1001
    파라미터  id=상세조회싶은 글번호
    모델	    질문  QuestionDTO
    view    templates/question_detail.html*/
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Integer id, Model model,
                         AnswerForm answerForm){
        //1.파라미터받기
        //2.비즈니스로직수행
        Question question = questionService.getQuestion(id);
        //3.Model
        model.addAttribute("question",question);
        //4.View
        return "question_detail"; //templates폴더하위  question_detail.html
    }


    //페이징기능있는 질문목록조회
    //요청주소   /quest/list?page=1
    //주의.springboot의 첫 페이지번호는 1이 아닌 0이다!!!!!!!!!!
    //주의.파라미터의 값은 문자열로 받는다
    @GetMapping("/list")
    public String questionList(Model model,
                               @RequestParam(value="page",defaultValue="0") int page){
        Page<Question> questionPage= this.questionService.getList(page);
        model.addAttribute("questionPage",questionPage);
        return "question_list";//  templates폴더하위  question_list.html문서
    }

    //질문추천
    //요청주소    /question/vote/질문번호
    //요청방식    get
    @PreAuthorize("isAuthenticated()")//로그인인증=>로그인이 필요한 기능
    @GetMapping("/vote/{id}")
    public String questionVote(@PathVariable("id") Integer id,Principal principal){
        Question question = questionService.getQuestion(id); //해당질문의 상세정보조회
        //principal.getName() //로그인한 유저
        //로그인한 유저를 이용하여  site_user테이블에서  유저의 상세정보를 가져오기=>새 추천인정보
        SiteUser siteUser= userService.getUser(principal.getName());    //추천인정보
        questionService.vote(question,siteUser);
        return String.format("redirect:/question/detail/%d",id);//(질문상세조회요청을 통한)질문상세페이지로 이동
    }

}
