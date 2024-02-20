package site.hclub.hyndai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import lombok.RequiredArgsConstructor;
import site.hclub.hyndai.domain.JwtToken;
import site.hclub.hyndai.domain.LoginRequest;
import site.hclub.hyndai.dto.SignInDto;
import site.hclub.hyndai.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	
	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody LoginRequest dto){
		return ResponseEntity.ok().body(userService.login(dto.getUserName()+"님의", ""));//아이디, 비밀번호
	}
	
	 @PostMapping("/login2")
	 public ResponseEntity<JwtToken> signIn(@RequestBody SignInDto signInDto) {
		 

	     String userId = signInDto.getUsername();
	     String password = signInDto.getPassword();
	     
		 JwtToken jwtToken = userService.signIn(userId, password);
		 HttpHeaders httpHeaders = new HttpHeaders();
	     httpHeaders.set("Authorization", "Bearer " + jwtToken.getAccessToken());
	     httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		 
	     return ResponseEntity.ok()
	                .headers(httpHeaders)
	                .body(jwtToken);
	 }
}
