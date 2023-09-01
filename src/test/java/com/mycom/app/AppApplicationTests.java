package com.mycom.app;

import com.mycom.app.question.repository.QuestionRepository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class AppApplicationTests {

	@Autowired
	private QuestionRepository questionRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void testJpa() {

/*		//더미데이터 입력
	     for(int i = 201; i <=211 ; i++){
         Question question = new Question();
         question.setSubject("제목"+i);
         question.setContent("내용"+i);
         question.setCreateDate( LocalDateTime.now() );
         questionRepository.save(question);
      */
/*
		//1개의 질문등록
		Question question = new Question();
		question.setSubject("질문제목1");
		question.setContent("질문내용1");
		question.setCreateDate(LocalDateTime.now());
		questionRepository.save(question);

		//1개의 질문등록
		Question question2 = new Question();
		question2.setSubject("질문제목2");
		question2.setContent("질문내용2");
		question2.setCreateDate(LocalDateTime.now());
		questionRepository.save(question2);
*/


/*		//assertEquals(예측값, 결과값)
		//예측값과 결과값이 일치하면 true반환=> test가 성공의미
		//예측값과 결과값이 불일치하면  test가 fail의미

		//질문 목록 조회 :findAll()
		List<Question> questionList = questionRepository.findAll();
		assertEquals(2, questionList.size()); //성공

		Question question= questionList.get(0); //전체목록에서 첫 번째 행을 가져와
		assertEquals( "질문제목1" ,question.getSubject());//su
		assertEquals( "~~~~~~1" ,question.getSubject());//fail*/

	/*	//상세조회 :findById(~~)
		//findById(10)는 Question테이블에서 id가 10인 Question Entity가져오기
		//Optional클래스 : java8에서 도입. java.util.Optional클래스
		Optional<Question> optionalQuestion = questionRepository.findById(10);
		if( optionalQuestion.isPresent() ){
			Question  question = optionalQuestion.get();
			assertEquals( "질문제목206",question.getSubject());//성공예측
			//assertEquals( "질문제목10",question.getSubject());//실패예측
		}*/

		//QuestionRepository에 선언한 findBySubject()테스트
		//questionRepository.findBySubject("질문제목1") : 제목이 "질문제목1"인 Question Entity(테이블에서) 가져오기
		//question2.getId(): 반환받은 Question Entity에서 id를 가져오기=> 글번호라는 의미..

		//Question question2 = questionRepository.findBySubject("질문제목1");
		//assertEquals(9,question2.getId()); //성공예측
		//assertEquals(900,question2.getId()); //실패예측

		//아래는 question테이블의 제목(subject)컬럼값의 어디엔가 제목1이 포함된 레코드를 조회
		//where X.subjcect like %?%
		//List<Question> findBySubjectLike(String subject)
/*		List<Question> questionList2
				= questionRepository.findBySubjectLike("%제목1%");
			//assertEquals(2,questionList2.size()); //성공예측
			assertEquals(1,questionList2.get(0).getId()); // 실패예측*/
/*


		//수정
	/*	Optional<Question> optionalQuestion2 = questionRepository.findById(9);
		if( optionalQuestion2.isPresent() ) { //id가 10인 Question Entity클래스가 존재하면
			Question question2 = optionalQuestion2.get(); // Question Entity클래스가져오기
			//assertEquals( "질문제목1",question2.getSubject());//성공예측
			question2.setSubject("변경된질문제목1");
			questionRepository.save(question2);
			assertEquals( "변경된질문제목1",question2.getSubject());//성공예측
			assertEquals( "질문제목1",question2.getSubject());//실패예측
		}
	*/

/*		//삭제  test
		//long count(); //전체 레코드수 조회
		//void delete(T entity); //삭제
		System.out.println("삭제전레코드수="+questionRepository.count());//콘솔출력
		assertEquals(2,questionRepository.count());//삭제전레코드수 성공예측

		Optional<Question> optionalQuestion3 = questionRepository.findById(9);
		//글번호가 9인 레코드가 존재하면 true=>리턴받은 값이 true이면 성공
		assertTrue( optionalQuestion3.isPresent() ); //성공예측
		Question question3 = optionalQuestion3.get();
		questionRepository.delete(question3);
		assertEquals(1,questionRepository.count());//삭제후레코드수 성공예측
		System.out.println("삭제전레코드후="+questionRepository.count()); //콘솔출력
	*/
	}


}