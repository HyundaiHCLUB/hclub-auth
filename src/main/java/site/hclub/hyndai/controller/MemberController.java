package site.hclub.hyndai.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.hclub.hyndai.domain.JwtToken;
import site.hclub.hyndai.dto.SignInDto;
import site.hclub.hyndai.service.MemberService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/login")
    public JwtToken signIn(@RequestBody SignInDto signInDto) {
    	
        String username = signInDto.getUsername();
        String password = signInDto.getPassword();
        log.info("username: "+username);
        JwtToken jwtToken = memberService.signIn(username, password);
        log.info("request username = {}, password = {}", username, password);
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());
        return jwtToken;
    }

    @PostMapping("/test")
    public String test() {
        return "success";
    }

}
