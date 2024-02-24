package site.hclub.hyndai.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;
import site.hclub.hyndai.config.RestTemplateConfig;
import site.hclub.hyndai.domain.Employee;
import site.hclub.hyndai.domain.JwtToken;
import site.hclub.hyndai.domain.MemberVO;
import site.hclub.hyndai.dto.EmployeeDTO;
import site.hclub.hyndai.dto.request.Hobbies;
import site.hclub.hyndai.dto.request.UpdateMemberInfoRequest;
import site.hclub.hyndai.dto.response.HobbiesClassifiedResponse;
import site.hclub.hyndai.dto.response.MyPageInfoResponse;
import site.hclub.hyndai.dto.response.MypageClubResponse;
import site.hclub.hyndai.dto.response.MypageMatchHistoryResponse;
import site.hclub.hyndai.mapper.ClubMapper;
import site.hclub.hyndai.mapper.MemberMapper;
import site.hclub.hyndai.mapper.TokenMapper;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 김은솔
 * @description: 멤버 관련 생성 serviceImpl
 * ===========================
	   AUTHOR      NOTE
 * ---------------------------
 *    김은솔        최초생성
 * ===========================
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberServiceImpl implements MemberService{
	
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    
    @Autowired
    private TokenMapper tokenMapper;
    
    @Autowired
    private MemberMapper memberMapper;

	private final ClubMapper clubMapper;

	private final RestTemplate restTemplate;

	@Override
	public int insertTokenInfo(JwtToken jwtToken, String userId) {
		
		MemberVO mvo = memberMapper.getMemberInfo(userId);
		jwtToken.setMemberNo(mvo.getMemberNo());
		
		return tokenMapper.insertTokenMemberInfo(jwtToken);
	}

	@Override
	public MemberVO getMemberInfo(String userId) {
		return memberMapper.getMemberInfo(userId);
	}

	@Override
	public String getEmployeeYn(EmployeeDTO dto) {
		
		int cnt = memberMapper.getEmployeeYn(dto);
		
		return cnt > 0 ? "Y":"N";
	}
	
	@Override
	public String resolveToken(HttpServletRequest request) {
	    	
	       String bearerToken = request.getHeader("Authorization");
	       if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
	           return bearerToken.substring(7);
	       }
	       return null;
	 }

	@Override
	public void insertMemberInfo(MemberVO mvo) {
		// 회원가입
		memberMapper.insertMemberInfo(mvo);
		log.info("HIIII" + memberMapper.getMemberInfo(mvo.getMemberId()).toString());
		// 추천 동아리 추가 로직
		Long memberNo = memberMapper.getMemberInfo(mvo.getMemberId()).getMemberNo();
		insertMemberClubInterest(memberNo,mvo.getMemberInterest());

	}

	@Override
	public MyPageInfoResponse getMypageUserInfo(String memberId) {
		MyPageInfoResponse respose = new MyPageInfoResponse();
		MemberVO vo = memberMapper.getMemberInfo(memberId);
		respose.setMember_id(vo.getMemberId());
		respose.setMemberImage(vo.getMemberImage());
		respose.setMemberInterest(vo.getMemberInterest());
		respose.setMemberRating(vo.getMemberRating());

		Employee emp = memberMapper.getEmployeeInfo(vo.getEmployeeNo());
		respose.setEmployeeName(emp.getEmployeeName());
		respose.setEmployeeDept(emp.getEmployeeDept());
		respose.setEmployeePosition(emp.getEmployeePosition());

		return respose;
	}

	@Override
	public MypageClubResponse getMypageClubInfo(String memberId) {
		MypageClubResponse response = memberMapper.getMypageClubInfo(memberId);
		return response;
	}

	@Override
	public void updateMemberInfo(UpdateMemberInfoRequest request) {
		memberMapper.updateUserPw(request);
	}

	@Override
	public List<MypageMatchHistoryResponse> getMypageMatchHistory(String memberId) {
		List<MypageMatchHistoryResponse> response = memberMapper.getMypageMatchHistory(memberId);
		log.info("=== Service ===");
		log.info("input(Service) : " + memberId);
		log.info("response -> " + response.toString());
		return response;
	}

	@Override
	public void insertMemberClubInterest(Long memberNo, String interests) {
		log.info("insertMemberClubInterest" + memberNo+" "+interests);
		String url = "http://localhost:8000/classify-hobbies";
		HttpHeaders httpHeaders  = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		List<String> interestList = Arrays.asList(interests.split(","));

		Map<String, List<String>> requestBody = new HashMap<>();
		requestBody.put("hobbies",interestList);

		HttpEntity<Map<String,List<String>>> request = new HttpEntity<>(requestBody, httpHeaders);

		HobbiesClassifiedResponse response = restTemplate.postForObject(url,request, HobbiesClassifiedResponse.class);
		log.info(response.getHobbies().toString());
		List<Integer> topInterestList = response.getHobbies();


		List<int[]> indexedNumbers = new ArrayList<>();
		for (int i = 0; i < topInterestList.size(); i++) {
			indexedNumbers.add(new int[]{topInterestList.get(i), i});
		}

		// 값에 따라 내림차순, 값이 같으면 인덱스에 따라 오름차순으로 정렬
		Collections.sort(indexedNumbers, (a, b) -> {
			if (a[0] != b[0]) {
				return b[0] - a[0]; // 값에 따른 내림차순 정렬
			} else {
				return a[1] - b[1]; // 인덱스에 따른 오름차순 정렬
			}
		});

		List<Integer> interestCategoryList = indexedNumbers.stream()
				.limit(3)
				.map(pair -> pair[1]+1)
				.collect(Collectors.toList());

		// 동아리 insert

		for(int interestNo : interestCategoryList){
		Long clubNo  = clubMapper.getClubByCategory(interestNo);
		clubMapper.insertMemberClubInterest(memberNo,clubNo);
		}

	}

}