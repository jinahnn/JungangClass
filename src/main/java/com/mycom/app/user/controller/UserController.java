package com.mycom.app.user.controller;

import com.mycom.app.user.service.UserService;
import com.mycom.app.user.validation.UserCreateForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/user")
@Controller
public class UserController {

    private final UserService userService;
    //private final PasswordEncoder passwordEncoder;

    //로그인폼을 보여줘요청
    /*요청주소  /user/login
      요청방식  get */
    @GetMapping("/login")
    public String login(){
        return "login_form"; //templates폴더하위의  login_form.html문서를 의미
    }



    //회원가입폼을 보여줘요청
    /*요청주소  /user/signup
      요청방식  get */
    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm){
        return "signup_form";//templates폴더하위의 signup_form.html문서
    }
    //회원가입처리 요청
    /*요청주소  /user/signup
      요청방식  post */
    @PostMapping("/signup")
    public String signup(@Valid UserCreateForm userCreateForm,
                         BindingResult bindingResult){
        if(bindingResult.hasErrors()){ //에러가 존재하면
            return "signup_form";//templates폴더하위의 signup_form.html문서를 보여줘
        }
        //2.비즈로직처리 
        //비밀번호와 비밀번호 확인을 서로 비교하여 불일치하면 signup_form.html 문서로이동
        if(!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())){ //에러가 존재하면
            bindingResult.rejectValue("password2","passwordIncorrect","비밀번호가 일치하지 않습니다.");

            return "signup_form";//templates폴더하위의 signup_form.html문서를 보여줘
        }
        //Service 값을 userCreateForm (유효성검사)하고 받아온다
        try {
            userService.create(userCreateForm.getUsername(),
                    userCreateForm.getEmail(),
                    userCreateForm.getPassword1());
        }catch (DataIntegrityViolationException e){
            //여기에서는 username(회원 id uk, email uk)-> 제약조건에 걸리면 발생
            e.printStackTrace();
            bindingResult.reject("singupFailed", "이미 등록된 회원입니다");
            return "signup_form"; // 실패하면 다시 회원가입 창
        }catch (Exception e){
            e.printStackTrace();
            bindingResult.reject("singupFailed",e.getMessage());
            return "signup_form"; // 실패하면 다시 회원가입 창
        }
        //3.Model //4.view
        return "redirect:/"; //회원가입성공시  메인화면으로 이동
    }
}
