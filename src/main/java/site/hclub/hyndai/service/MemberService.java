package site.hclub.hyndai.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;
import site.hclub.hyndai.domain.JwtToken;
import site.hclub.hyndai.domain.MemberVO;
import site.hclub.hyndai.dto.EmployeeDTO;
import site.hclub.hyndai.dto.request.UpdateMemberInfoRequest;
import site.hclub.hyndai.dto.response.MyPageInfoResponse;
import site.hclub.hyndai.dto.response.MypageClubResponse;
import site.hclub.hyndai.dto.response.MypageMatchHistoryResponse;

import java.io.IOException;
import java.util.List;

public interface MemberService {
	
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

	MypageClubResponse getMypageClubInfo(String memberId);

	void updateMemberInfo(UpdateMemberInfoRequest request);

	List<MypageMatchHistoryResponse> getMypageMatchHistory(String memberId);

	String updateProfileImage(MultipartFile multipartFile, String memberId) throws IOException;
}
