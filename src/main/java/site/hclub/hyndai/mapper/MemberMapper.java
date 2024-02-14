package site.hclub.hyndai.mapper;

import site.hclub.hyndai.domain.MemberVO;
import site.hclub.hyndai.dto.EmployeeDTO;

public interface MemberMapper {

	/**
	 작성자: 김은솔 
	 처리 내용: 멤버 아이디를 기준으로 멤버 정보를 조회한다.
	*/
	MemberVO getMemberInfo(String userId);
	
	/**
	 작성자: 김은솔 
	 처리 내용: 멤버 아이디를 기준으로 해당 아이디가 존재하는지를 조회한다.
	*/
	int getMemberIdYn(String userId);
	
	/**
	 작성자: 김은솔 
	 처리 내용: 사원번호를 기준으로 해당 사원이 존재하는지를 조회한다.
	*/
	int getEmployeeYn(EmployeeDTO dto);
	/**
	 작성자: 김은솔 
	 처리 내용: 멤버정보를 바탕으로 회원가입한다.
	*/
	int insertMemberInfo(MemberVO mvo);
	
}
