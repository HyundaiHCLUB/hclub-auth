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
import site.hclub.hyndai.dto.request.UpdateMemberInfoRequest;
import site.hclub.hyndai.dto.response.MyPageInfoResponse;
import site.hclub.hyndai.dto.response.MypageClubResponse;
import site.hclub.hyndai.service.MemberService;
import site.hclub.hyndai.service.UserService;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

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
	private final UserService userService;
   
    @PostMapping("/login")
    public ResponseEntity<JwtToken> signIn(@RequestBody SignInDto signInDto) {
     
    	String userId = signInDto.getUsername();
	     String password = signInDto.getPassword();
	     
		 JwtToken jwtToken = userService.signIn(userId, password);
	
		 //토큰 정보를 삽입한다.
		 memberService.insertTokenInfo(jwtToken, userId);
		 
		 HttpHeaders httpHeaders = new HttpHeaders();
	     httpHeaders.set("Authorization", "Bearer " + jwtToken.getAccessToken());
	     httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		 
	     return ResponseEntity.ok()
	                .headers(httpHeaders)
	                .body(jwtToken);
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
    	
    	memberService.insertMemberInfo(mvo);
    	
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
 
    	String userId =  principal.getName();
       	log.info( "test getId: "+ userId);
    	 return ResponseEntity.ok(map);
    }


    /*** 마이페이지 ****/

    /**
     *  마이페이지 - 기본 인적사항
     *  @respnse
     *  - MyPageInfoResponse : 이름, 부서명, 직급, 아이디, 사진, 흥미, 레이팅
     */
    @GetMapping("/mypage/info")
    public ResponseEntity<MyPageInfoResponse> getMypageUserInfo(Principal principal, HttpServletRequest request)
    {
        // 1. 헤더에서 멤버아이디 가져오기
        // "Authorization" 헤더에서 토큰을 추출합니다.
        String token = request.getHeader("Authorization");
        log.info("token : " + token);
        String memberId = "";
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // "Bearer "을 제거합니다.
            try {
                memberId = principal.getName();
                log.info("get Member ID : " + memberId);
                // userId를 ResponseEntity에 담아 반환합니다.
            } catch (Exception e) {
                // 토큰이 유효하지 않은 경우
                log.error(e.getMessage());
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Invalid token");
            }
        } else {
            // 헤더에 Bearer 토큰이 없는 경우.
            log.info("=== token is NULL ===");
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Authorization header is missing or not in Bearer format");
        }
        // 2. 해당 유저 정보
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

    /**
     *  마이페이지 - 회원정보 수정(비밀번호 only)
     */
    @PostMapping(value = "/mypage",
                 consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateUserInfo(@RequestBody UpdateMemberInfoRequest request) {

        try{
            memberService.updateMemberInfo(request);
        }catch (Exception e){
            return new ResponseEntity<>("failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @GetMapping("/aa")
    public ResponseEntity<String> getTest(String interest) {

        memberService.insertMemberClubInterest(1L,interest);
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

}
