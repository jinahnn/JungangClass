package com.mycom.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="entity not found")

//조회 결과가 없을 경우 발생하는 예외처리 클래스 : 404예외
public class DataNotFoundException extends RuntimeException {

   //생성자
    public DataNotFoundException(String msg) {
        super(msg);  //super는 생성자 내부에 선언해줘야한다/
    }
}
