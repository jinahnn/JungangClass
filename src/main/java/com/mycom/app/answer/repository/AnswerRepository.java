package com.mycom.app.answer.repository;

import com.mycom.app.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

//Reprository는  Entity에 의하여 생성된 db에 접근하여
//작업하는 여러 메서드들로 구성된 interface이다
//=>CRUD

// JpaRepository인터페이스를 상속하고 있다
// <Repository의 대상이 되는 Entity타입,  Entity타입의 PK타입>
//여기에서는 <Question,Integer> Repository의 대상이 되는 Answer의 PK는  Integer
public interface AnswerRepository extends JpaRepository<Answer,Integer> {
}
