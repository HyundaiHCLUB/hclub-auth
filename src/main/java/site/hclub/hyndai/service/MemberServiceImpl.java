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
import site.hclub.hyndai.mapper.MemberMapper;
import site.hclub.hyndai.mapper.TokenMapper;

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
    
    private final JwtTokenProvider jwtTokenProvider;
    
    @Autowired
    private TokenMapper TokenMapper;
    
    @Autowired
    private MemberMapper memberMapper;

    @Transactional
    @Override
    public JwtToken signIn(String userId, String password) {
    	
        // 1. userId + password 를 기반으로 Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, password);
        
        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행 =>  CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // SecurityContextHolder.getContext().setAuthentication(authentication);
       
        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);
        
        // 4. 생성된 JWT토큰 정보를 jwt관련 테이블로 insert 
        //insertTokenInfo(jwtToken, userId);

        return jwtToken;
    }

	@Override
	public int insertTokenInfo(JwtToken jwtToken, String userId) {
		
		MemberVO mvo = memberMapper.getMemberInfo(userId);
		jwtToken.setMemberNo(mvo.getMemberNo());
		
		return TokenMapper.insertTokenMemberInfo(jwtToken);
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
	public MemberVO accessMemberInfo(HttpServletRequest request) {
		
		String accessToken = resolveToken(request);
		
		//accessToken을 통해 권한을 가져옴.
		Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
		
		String userId = authentication.getName();
		
		return memberMapper.getMemberInfo(userId);
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
}