package site.hclub.hyndai.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import site.hclub.hyndai.common.util.AmazonS3Service;
import site.hclub.hyndai.domain.Employee;
import site.hclub.hyndai.domain.JwtToken;
import site.hclub.hyndai.domain.MemberVO;
import site.hclub.hyndai.dto.EmployeeDTO;
import site.hclub.hyndai.dto.request.UpdateMemberInfoRequest;
import site.hclub.hyndai.dto.response.HobbiesClassifiedResponse;
import site.hclub.hyndai.dto.response.MyPageInfoResponse;
import site.hclub.hyndai.dto.response.MypageClubResponse;
import site.hclub.hyndai.dto.response.MypageMatchHistoryResponse;
import site.hclub.hyndai.mapper.ClubMapper;
import site.hclub.hyndai.mapper.MemberMapper;
import site.hclub.hyndai.mapper.TokenMapper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 김은솔
 * @description: 멤버 관련 생성 serviceImpl
 * ===========================
 * AUTHOR      NOTE
 * ---------------------------
 * 김은솔        최초생성
 * ===========================
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final ClubMapper clubMapper;
    private final RestTemplate restTemplate;
    private final AmazonS3Service amazonS3Service;
    @Autowired
    private TokenMapper tokenMapper;
    @Autowired
    private MemberMapper memberMapper;

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

        return cnt > 0 ? "Y" : "N";
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

    // 마이페이지 - 프로필 사진 수정
    @Override
    public String updateProfileImage(MultipartFile multipartFile, String memberId) throws IOException {
        String url;
        /* S3 업로드 */
        String filePath = "profile";
        List<MultipartFile> multipartFiles = new ArrayList<>();
        System.out.println("=========== SERVICE =============");
        System.out.println("ORIGINAL FILE NAME : " + multipartFile.getOriginalFilename());
        multipartFiles.add(multipartFile);
        // uploadFiles 메서드를 사용하여 파일 업로드
        List<String> urls = amazonS3Service.uploadFiles(filePath, multipartFiles);
        // 반환된 URL 리스트에서 첫 번째 URL을 사용
        url = urls.get(0);
        log.info(url);
        /* DB 에 파일 URL 업로드*/
        String fileName = multipartFile.getOriginalFilename();
        memberMapper.updateProfileImage(url, memberId);
        return url;
    }

    @Override
    public void insertMemberClubInterest(String memberId, String interests) {

        // 추천 동아리 추가 로직
        Long memberNo = memberMapper.getMemberInfo(memberId).getMemberNo();


        log.info("insertMemberClubInterest" + memberNo + " " + interests);
        String url = "https://www.h-club.site/ai/classify-hobbies";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<String> interestList = Arrays.asList(interests.split(","));

        Map<String, List<String>> requestBody = new HashMap<>();
        requestBody.put("hobbies", interestList);


        HttpEntity<Map<String, List<String>>> request = new HttpEntity<>(requestBody, httpHeaders);

        HobbiesClassifiedResponse response = restTemplate.postForObject(url, request, HobbiesClassifiedResponse.class);
        log.info(response.getHobbies().toString());
        List<Integer> topInterestList = response.getHobbies();
        topInterestList.remove(topInterestList.size() - 1);
        log.info(topInterestList.toString());

        List<int[]> indexedNumbers = new ArrayList<>();
        for (int i = 0; i < topInterestList.size(); i++) {
            indexedNumbers.add(new int[]{topInterestList.get(i), i});
        }

        // 값에 따라 내림차순, 값이 같으면 인덱스에 따라 오름차순으로 정렬
        Collections.sort(indexedNumbers, (a, b) -> {
            if (a[0] != b[0]) {
                return b[0] - a[0];
            } else {
                return a[1] - b[1];
            }
        });

        List<Integer> interestCategoryList = indexedNumbers.stream()
                .limit(4)
                .map(pair -> pair[1] + 1)
                .collect(Collectors.toList());

        // 동아리 insert

        for (int interestNo : interestCategoryList) {
            Long clubNo = clubMapper.getClubByCategory(interestNo);
            clubMapper.insertMemberClubInterest(memberNo, clubNo);
        }

    }

}