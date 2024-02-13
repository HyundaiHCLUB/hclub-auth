package site.hclub.hyndai.mapper;

import site.hclub.hyndai.domain.MemberVO;

public interface MemberMapper {

	/**
	 작성자: 김은솔 
	 처리 내용: 멤버 아이디를 바탕으로 멤버 정보를 조회한다.
	*/
	MemberVO getMemberInfo(String userId);
	
}
