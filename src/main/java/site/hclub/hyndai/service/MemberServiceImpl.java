package site.hclub.hyndai.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.hclub.hyndai.domain.Employee;
import site.hclub.hyndai.domain.JwtToken;
import site.hclub.hyndai.domain.MemberVO;
import site.hclub.hyndai.dto.EmployeeDTO;
import site.hclub.hyndai.dto.request.UpdateMemberInfoRequest;
import site.hclub.hyndai.dto.response.MyPageInfoResponse;
import site.hclub.hyndai.dto.response.MypageClubResponse;
import site.hclub.hyndai.dto.response.MypageMatchHistoryResponse;
import site.hclub.hyndai.mapper.MemberMapper;
import site.hclub.hyndai.mapper.TokenMapper;

import java.util.List;

/**
 * @author 김은솔
 * @description: 멤버 관련 생성 serviceImpl
 * ===========================
	   AUTHOR      NOTE
 * ---------------------------
 *    김은솔        최초생성
 * ===========================
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberServiceImpl implements MemberService{
	
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    
    @Autowired
    private TokenMapper tokenMapper;
    
    @Autowired
    private MemberMapper memberMapper;

	@Override
	public int insertTokenInfo(JwtToken jwtToken, String userId) {
		
		MemberVO mvo = memberMapper.getMemberInfo(userId);
		jwtToken.setMemberNo(mvo.getMemberNo());
		
		return tokenMapper.insertTokenMemberInfo(jwtToken);
	}

	@Override
	public MemberVO getMemberInfo(String userId) {
		return memberMapper.getMemberInfo(userId);
	}

	@Override
	public String getEmployeeYn(EmployeeDTO dto) {
		
		int cnt = memberMapper.getEmployeeYn(dto);
		
		return cnt > 0 ? "Y":"N";
	}
	
	@Override
	public String resolveToken(HttpServletRequest request) {
	    	
	       String bearerToken = request.getHeader("Authorization");
	       if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
	           return bearerToken.substring(7);
	       }
	       return null;
	 }

	@Override
	public int insertMemberInfo(MemberVO mvo) {
		
		return memberMapper.insertMemberInfo(mvo);
	}

	@Override
	public MyPageInfoResponse getMypageUserInfo(String memberId) {
		MyPageInfoResponse respose = new MyPageInfoResponse();
		MemberVO vo = memberMapper.getMemberInfo(memberId);
		respose.setMember_id(vo.getMemberId());
		respose.setMemberImage(vo.getMemberImage());
		respose.setMemberInterest(vo.getMemberInterest());
		respose.setMemberRating(vo.getMemberRating());

		Employee emp = memberMapper.getEmployeeInfo(vo.getEmployeeNo());
		respose.setEmployeeName(emp.getEmployeeName());
		respose.setEmployeeDept(emp.getEmployeeDept());
		respose.setEmployeePosition(emp.getEmployeePosition());

		return respose;
	}

	@Override
	public MypageClubResponse getMypageClubInfo(String memberId) {
		MypageClubResponse response = memberMapper.getMypageClubInfo(memberId);
		return response;
	}

	@Override
	public void updateMemberInfo(UpdateMemberInfoRequest request) {
		memberMapper.updateUserPw(request);
	}

	@Override
	public List<MypageMatchHistoryResponse> getMypageMatchHistory(String memberId) {
		List<MypageMatchHistoryResponse> response = memberMapper.getMypageMatchHistory(memberId);
		log.info("=== Service ===");
		log.info("input(Service) : " + memberId);
		log.info("response -> " + response.toString());
		return response;
	}

}