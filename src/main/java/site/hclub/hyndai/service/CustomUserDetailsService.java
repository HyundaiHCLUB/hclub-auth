package site.hclub.hyndai.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import site.hclub.hyndai.domain.MemberVO;
import site.hclub.hyndai.mapper.MemberMapper;
import org.springframework.security.core.userdetails.User;
import java.util.stream.Collectors;

/**
 * @author 김은솔
 * @description: SpringSecurity관련 CustomUserservice
 * ===========================
	   AUTHOR      NOTE
 * ---------------------------
 *    김은솔        최초생성
 * ===========================
 */
@Service
@RequiredArgsConstructor
@Log4j
public class CustomUserDetailsService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final MemberMapper memberMapper;

    
    /**
     멤버 아이디를 바탕으로 해당 유저의 존재 여부를 조회한다.
     */
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        MemberVO member = memberMapper.getMemberInfo(userId);
        if (member == null) {
            throw new UsernameNotFoundException("해당하는 회원을 찾을 수 없습니다.");
        }
        return createUserDetails(member);
    }

    // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 return
    private UserDetails createUserDetails(MemberVO member) {
    	log.info(member.toString());
        return User.builder()
                .username(member.getUsername())
                .password(passwordEncoder.encode(member.getPassword()))
                .roles(member.getRoles().toArray(new String[0]))
                .build();
    }
}
