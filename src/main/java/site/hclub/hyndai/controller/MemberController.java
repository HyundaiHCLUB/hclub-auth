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
import site.hclub.hyndai.common.response.ApiResponse;
import site.hclub.hyndai.common.response.SuccessType;
import site.hclub.hyndai.domain.JwtToken;
import site.hclub.hyndai.domain.MemberVO;
import site.hclub.hyndai.dto.EmployeeDTO;
import site.hclub.hyndai.dto.SignInDto;
import site.hclub.hyndai.service.JwtTokenProvider;
import site.hclub.hyndai.service.MemberService;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import static site.hclub.hyndai.common.response.SuccessType.*;

/**
 * @author 김은솔
 * @description: Security + JWT 기반 MemberController
 * ===========================
	   AUTHOR      NOTE
 * ---------------------------
 *    김은솔       최초 생성
 * ===========================
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class MemberController {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<JwtToken> signIn(@RequestBody SignInDto signInDto) {
        //  ResponseEntity<ApiResponse<Club>>
    	 //  ResponseEntity<ApiResponse<Club>> 
        String userId = signInDto.getUsername();
        String password = signInDto.getPassword();
        log.info("signInDto: "+signInDto.toString());
        JwtToken jwtToken = memberService.signIn(userId, password);
        
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + jwtToken.getAccessToken());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        log.info("httpHeaders = {}", httpHeaders);
        //    return ApiResponse.success(SuccessType.CREATE_CLUB_SUCCESS, clubService.save(image, request));
        return ResponseEntity.ok()
                .headers(httpHeaders)
                .body(jwtToken);
    }

    @PostMapping("/member/info")
    public ResponseEntity<ApiResponse<MemberVO>> accessMemberInfo(HttpServletRequest authorizationHeader) {
       
       return ApiResponse.success(GET_MEMBER_DETAIL_SUCCESS, memberService.accessMemberInfo(authorizationHeader));
    }

    @GetMapping("/loginView")
    public ModelAndView loginView() {
    	ModelAndView mv = new ModelAndView();
    	mv.setViewName("auth/loginView");
    	
    	return mv;
    }
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> accessMemberInfo(@RequestBody MemberVO mvo){
    	Map<String, Object> response = new HashMap<>();
    	
    	int result = memberService.insertMemberInfo(mvo);
    	
    	return ApiResponse.success(INSERT_MEMBER_INFO_SUCCESS);
    }
    

    @PostMapping("/getEmployeeYn")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getEmployeeYn(@RequestBody EmployeeDTO dto) {
        
    	Map<String, Object> response = new HashMap<>();
        String isEmployee = memberService.getEmployeeYn(dto);
        response.put("isEmployee", isEmployee);
        
        return ApiResponse.success(GET_EMPLOYEE_YN_SUCCESS, response);
    }
}
