package site.hclub.hyndai.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.hclub.hyndai.domain.JwtToken;
import site.hclub.hyndai.utils.JwtUtil;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
   private final AuthenticationManagerBuilder authenticationManagerBuilder;
   private final JwtTokenProvider jwtTokenProvider;
   
	@Value("${jwt-secret}")
	private String secretKey;
	
	private Long expiredMs = 1000 * 60 * 60l;
	
	public String login(String userName, String password) {
		//인증과정 생략
		return JwtUtil.createJwt(userName, secretKey,expiredMs);
	}

	public JwtToken signIn(String userId, String password) {
		
		UsernamePasswordAuthenticationToken authenticationToken
		= new UsernamePasswordAuthenticationToken(userId, password, List.of(new SimpleGrantedAuthority("USER")));
		
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
		JwtToken jwtToken = jwtTokenProvider.generateToken(authentication);
	        
		// return jwtToken;
        
		return jwtToken;
	}

}
