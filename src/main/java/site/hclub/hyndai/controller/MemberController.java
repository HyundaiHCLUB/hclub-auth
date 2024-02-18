package site.hclub.hyndai.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import site.hclub.hyndai.common.response.ApiResponse;
import site.hclub.hyndai.domain.JwtToken;
import site.hclub.hyndai.domain.MemberVO;
import site.hclub.hyndai.dto.EmployeeDTO;
import site.hclub.hyndai.dto.SignInDto;
import site.hclub.hyndai.dto.response.MyPageInfoResponse;
import site.hclub.hyndai.dto.response.MypageClubResponse;
import site.hclub.hyndai.service.JwtTokenProvider;
import site.hclub.hyndai.service.MemberService;

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
     
        String userId = signInDto.getUsername();
        String password = signInDto.getPassword();
      
        JwtToken jwtToken = memberService.signIn(userId, password);
        
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Authorization", "Bearer " + jwtToken.getAccessToken());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       Object principal = SecurityContextHolder.getContext();
      // Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
       // UserDetails userDetails = (UserDetails)principal; 
    
        
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
    @PostMapping("/test")
    public ResponseEntity<Map<String, Object>> test(Principal principal , HttpServletRequest authorizationHeader){
    	Map<String, Object> map =new HashMap<>();
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      //   Object principal = SecurityContextHolder.getContext();
    	 return ResponseEntity.ok(map);
    }

    /* 마이페이지 */
    /**
     *  마이페이지 - 기본 인적사항
     */
    @GetMapping("/mypage/info/{member_id}")
    public ResponseEntity<MyPageInfoResponse> getMypageUserInfo(@PathVariable("member_id")String memberId){
        log.info("mypage (userInfo) ==>");
        MyPageInfoResponse response = new MyPageInfoResponse();
        try{
            response = memberService.getMypageUserInfo(memberId);
            log.info(response.toString());
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /***
     *  마이페이지 - 내가 속한 동아리
     */
    @GetMapping("/mypage/club/{member_id}")
    public ResponseEntity<MypageClubResponse> getMypageClubInfo(@PathVariable("member_id") String memberId){
        MypageClubResponse response = new MypageClubResponse();
        try{
            response = memberService.getMypageClubInfo(memberId);
            log.info(response.toString());
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /***
     *  마이페이지 - 매치 히스토리
     */
//
//    @GetMapping("/mypage/comp/{member_id}")
//    public ResponseEntity<> getMypageCompInfo(@PathVariable("member_id") String memberId){
//
//    }


}
