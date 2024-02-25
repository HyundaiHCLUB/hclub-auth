package site.hclub.hyndai.config;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.hclub.hyndai.service.JwtTokenProvider;
import site.hclub.hyndai.service.UserService;

/**
 * @author 김은솔
 * @description: JwtFilter 설정
 * ===========================
	   AUTHOR      NOTE
 * ---------------------------
 *     김은솔        최초생성    
 * ===========================
 */
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
	
	private final UserService userService;
	
	@Value("${jwt-secret}")
	private final String secretKey;
	
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		//token정보를 가져와서 resolve한다.
		 String token =  resolveToken(request);     
         log.info("token: "+ token);
        // 2. validateToken으로 토큰 유효성 검사  
        if (token != null && jwtTokenProvider.validateToken(token) ) {
            // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext에 저장
        	Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
	}

	//토큰 정보 resolve하는 메소드
	public String resolveToken(HttpServletRequest request) {
	    	
	       String bearerToken = request.getHeader("Authorization");
	       if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
	           return bearerToken.substring(7);
	       }
	       return null;
	 }
}
