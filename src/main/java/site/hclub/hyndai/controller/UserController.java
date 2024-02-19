package site.hclub.hyndai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import site.hclub.hyndai.domain.LoginRequest;
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
}
