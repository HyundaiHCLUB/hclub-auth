package site.hclub.hyndai.service;

import site.hclub.hyndai.domain.JwtToken;

public interface MemberService {
	
	/**
	 작성자: 김은솔 
	 처리 내용: 멤버 정보를 조회하고, JwtToken을 발급한다.
	*/
	 JwtToken signIn(String userId, String password) ;
	 
	 /**
	 작성자: 김은솔 
	 처리 내용: 발급된 JwtToken과 userId를 Token테이블에 isnert한다.
	*/
	 int insertTokenInfo(JwtToken jwtToken, String userId);
}
