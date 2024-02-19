package site.hclub.hyndai.service;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@RequiredArgsConstructor
@Log4j
public class JwtAuthenticationFilter extends OncePerRequestFilter  {
	
    private final JwtTokenProvider jwtTokenProvider;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Request Header에서 JWT 토큰 추출
        //String token = memberService.resolveToken((HttpServletRequest) request);
         String token =  resolveToken(request);    
         log.info("token: "+ token);
        // 2. validateToken으로 토큰 유효성 검사  && jwtTokenProvider.validateToken(token)
        if (token != null ) {
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
