package site.hclub.hyndai.service;

import site.hclub.hyndai.domain.JwtToken;

public interface MemberService {
	
	 JwtToken signIn(String userId, String password) ;
}
