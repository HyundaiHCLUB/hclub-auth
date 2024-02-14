package site.hclub.hyndai.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.hclub.hyndai.domain.JwtToken;
import site.hclub.hyndai.domain.MemberVO;
import site.hclub.hyndai.dto.EmployeeDTO;
import site.hclub.hyndai.dto.SignInDto;
import site.hclub.hyndai.service.JwtTokenProvider;
import site.hclub.hyndai.service.MemberService;
import site.hclub.hyndai.service.SecurityUtil;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

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
    private final JwtTokenProvider jwtTokenProvider;

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
    public ResponseEntity<MemberVO> accessMemberInfo(HttpServletRequest authorizationHeader) {
    
       MemberVO memberInfo = memberService.accessMemberInfo(authorizationHeader);
    	
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
    @PostMapping("/getEmployeeYn")
    public ResponseEntity<Map<String, Object>> getEmployeeYn(@RequestBody EmployeeDTO dto) {
        Map<String, Object> response = new HashMap<>();
        
        String isEmployee = memberService.getEmployeeYn(dto);
        
        response.put("isEmployee", isEmployee);
        
        return ResponseEntity.ok(response);
    }
}
