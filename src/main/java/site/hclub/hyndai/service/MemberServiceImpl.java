package site.hclub.hyndai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.hclub.hyndai.domain.JwtToken;
import site.hclub.hyndai.domain.MemberVO;
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

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);
        
        // 4. 생성된 JWT토큰 정보를 jwt관련 테이블로 insert 
        insertTokenInfo(jwtToken, userId);

        return jwtToken;
    }

	@Override
	public int insertTokenInfo(JwtToken jwtToken, String userId) {
		
		MemberVO mvo = memberMapper.getMemberInfo(userId);
		jwtToken.setMemberNo(mvo.getMemberNo());
		
		return TokenMapper.insertTokenMemberInfo(jwtToken);
	}
}