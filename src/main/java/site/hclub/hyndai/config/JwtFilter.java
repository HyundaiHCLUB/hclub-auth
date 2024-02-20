package site.hclub.hyndai.config;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.common.net.HttpHeaders;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.hclub.hyndai.service.JwtTokenProvider;
import site.hclub.hyndai.service.UserService;
import site.hclub.hyndai.utils.JwtUtil;

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
		
	
	//exapmple(request, response , filterChain);
	 exapmple2(request, response , filterChain);
	}
	protected void exapmple (HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)throws ServletException, IOException {
		final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
		log.info("authentication: {}", authorization);
		
		//토큰 안보내면 block
		if(authorization == null || !authorization.startsWith("Bearer ")) {
			log.info("authentication이 없습니다.  ");
			filterChain.doFilter(request, response);
			return;
		}
		//Token 꺼내기
		String token = authorization.split(" ")[1];
		
		//Token Expired되었는지 여부
		if(JwtUtil.isExpired(token, secretKey)) {
			log.error("Token이 만료 되었습니다.");
			filterChain.doFilter(request, response);
			return ;
		}
		
		//UserName Token에서 꺼내기 
		String userName=JwtUtil.getUserName(token, secretKey);
		log.info("userName:{}: ", userName);
		
		//권한 부여
		UsernamePasswordAuthenticationToken authenticationToken
		= new UsernamePasswordAuthenticationToken(userName, null, List.of(new SimpleGrantedAuthority("USER")));
		
		//Detail을 넣어줍니다.
		authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		filterChain.doFilter(request, response);
	
	}
	protected void exapmple2(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)throws ServletException, IOException {
		 String token =  resolveToken(request);    
         log.info("token: "+ token);
        // 2. validateToken으로 토큰 유효성 검사  && jwtTokenProvider.validateToken(token)
        if (token != null && jwtTokenProvider.validateToken(token) ) {
            // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext에 저장
        	Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
	}
	 public String resolveToken(HttpServletRequest request) {
	    	
	       String bearerToken = request.getHeader("Authorization");
	       if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
	           return bearerToken.substring(7);
	       }
	       return null;
	 }

}
