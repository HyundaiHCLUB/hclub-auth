package site.hclub.hyndai.service;

import javax.servlet.http.HttpServletRequest;

import site.hclub.hyndai.domain.JwtToken;
import site.hclub.hyndai.domain.MemberVO;
import site.hclub.hyndai.dto.EmployeeDTO;
import site.hclub.hyndai.dto.response.MyPageInfoResponse;

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
	 /**
	 작성자: 김은솔 
	 처리 내용: 멤버 정보를 확인한다.
	*/
	 MemberVO getMemberInfo(String userId);
	/**
	 작성자: 김은솔 
	 처리 내용: 직원여부를 조회한다.
	*/
	String getEmployeeYn(EmployeeDTO dto);
	/**
	  작성자: 김은솔 
	  처리 내용: accessToken을 통해 권한 정보를 가진 사용자 정보를 가져온다.
	*/
	MemberVO accessMemberInfo(HttpServletRequest authorizationHeader);
	/**
	  작성자: 김은솔 
	  처리 내용: Header에 담긴 token정보를 resolve한다.
	*/
	String resolveToken(HttpServletRequest request);
	/**
	  작성자: 김은솔 
	  처리 내용: 회원 정보를 바탕으로 Member테이블에 삽입한다.
	*/
	int insertMemberInfo(MemberVO mvo);

	/* 마이페이지 - 기본 인적사항*/
	MyPageInfoResponse getMypageUserInfo(String memberId);
}
