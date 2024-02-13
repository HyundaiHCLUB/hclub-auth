package site.hclub.hyndai.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.hclub.hyndai.domain.JwtToken;
import site.hclub.hyndai.domain.MemberVO;
import site.hclub.hyndai.dto.SignInDto;
import site.hclub.hyndai.service.MemberService;
import site.hclub.hyndai.service.SecurityUtil;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<JwtToken> signIn(@RequestBody SignInDto signInDto) {
        
        String userId = signInDto.getUsername();
        String password = signInDto.getPassword();
        log.info("signInDto: "+signInDto.toString());
        JwtToken jwtToken = memberService.signIn(userId, password);
        
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + jwtToken.getAccessToken());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        log.info("httpHeaders = {}", httpHeaders);
        
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(jwtToken);
    }

    
    @PostMapping("/member/info")
    public ResponseEntity<MemberVO> memberInfo(@AuthenticationPrincipal UserDetails userDetails) {
       

        log.info("userDetails: " + userDetails.toString());
        String userId = userDetails.getUsername();
        MemberVO memberInfo = memberService.getMemberInfo(userId);
        return ResponseEntity.ok(memberInfo);
    }



    @GetMapping("/loginView")
    public ModelAndView loginView() {
    	ModelAndView mv = new ModelAndView();
    	mv.setViewName("auth/loginView");
    	
    	return mv;
    }
    
    @PostMapping("/test")
    public String test() {
        return SecurityUtil.getCurrentUsername();
    }
}
