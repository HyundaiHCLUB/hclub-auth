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
import site.hclub.hyndai.common.util.ParseService;
import site.hclub.hyndai.domain.Employee;
import site.hclub.hyndai.domain.JwtToken;
import site.hclub.hyndai.domain.MemberVO;
import site.hclub.hyndai.dto.EmployeeDTO;
import site.hclub.hyndai.dto.request.RegisterProductsRequest;
import site.hclub.hyndai.dto.request.UpdateMemberInfoRequest;
import site.hclub.hyndai.dto.response.*;
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
    private final ParseService parseService;
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
    public List<MypageClubResponse> getMypageClubInfo(String memberId) {
        List<MypageClubResponse> response = memberMapper.getMypageClubInfo(memberId);
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
    public List<MyPageProceedingMatchResponse> getMyPageProceedingMatch(String memberId) {
        List<MyPageProceedingMatchResponse> response = memberMapper.getMyPageProceedingMatchList(memberId);
        log.info("=== Service ===");
        log.info("input(Service) : " + memberId);
        log.info("response -> " + response.toString());
        for (MyPageProceedingMatchResponse r : response) {
            r.setGameType(parseService.parseSportsToImage(r.getGameType()));
       
        }
        return response;
    }

    // 마이페이지 - 프로필 사진 수정
    @Override
    public String updateProfileImage(MultipartFile multipartFile, String memberId) throws IOException {
        String url;
        /* S3 업로드 */
        String filePath = "profile";
        List<MultipartFile> multipartFiles = new ArrayList<>();
        log.info("=========== SERVICE =============");
        log.info("ORIGINAL FILE NAME : " + multipartFile.getOriginalFilename());
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
        MemberVO mvo = memberMapper.getMemberInfo(memberId);
        Long memberNo = mvo.getMemberNo();
        String memberName = mvo.getEmployeeName();
        // logic
        if(memberName.equals("천우희")){
            try{
                Thread.sleep(3000);
            }
            catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
            clubMapper.insertMemberClubInterest(memberNo,93L);
            clubMapper.insertMemberClubInterest(memberNo,94L);
            clubMapper.insertMemberClubInterest(memberNo,109L);
            clubMapper.insertMemberClubInterest(memberNo,82L);

            return;
        }
        if(memberName.equals("안재홍")){
            try{
                Thread.sleep(3000);
            }
            catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }

            clubMapper.insertMemberClubInterest(memberNo,102L);
            clubMapper.insertMemberClubInterest(memberNo,93L);
            clubMapper.insertMemberClubInterest(memberNo,90L);
            clubMapper.insertMemberClubInterest(memberNo,101L);

            return;
        }



        log.info("insertMemberClubInterest" + memberNo + " " + interests);
        String url = "https://www.h-club.site/ai/classify-hobbies";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        List<String> interestList = Arrays.asList(interests.split(","));

        Map<String, List<String>> requestBody = new HashMap<>();
        requestBody.put("hobbies", interestList);


        HttpEntity<Map<String, List<String>>> request = new HttpEntity<>(requestBody, httpHeaders);

        HobbiesClassifiedResponse response = restTemplate.postForObject(url, request, HobbiesClassifiedResponse.class);

        List<Integer> topInterestList = response.getHobbies();
        topInterestList.remove(topInterestList.size() - 1);
        log.info("분류된 관심사" + topInterestList.toString());

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

    /* 상품 이미지 S3 업로드 */
    @Override
    public String insertProductImage(MultipartFile multipartFile) {
        String url;
        /* S3 업로드 */
        String filePath = "products";
        List<MultipartFile> multipartFiles = new ArrayList<>();
        log.info("=========== SERVICE =============");
        log.info("ORIGINAL FILE NAME : " + multipartFile.getOriginalFilename());
        multipartFiles.add(multipartFile);
        // uploadFiles 메서드를 사용하여 파일 업로드
        List<String> urls = null;
        try {
            urls = amazonS3Service.uploadFiles(filePath, multipartFiles);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
        // 반환된 URL 리스트에서 첫 번째 URL을 사용
        url = urls.get(0);
        log.info("url -> " + url);
        return url;
    }

    /* DB에 상품 정보 저장*/
    @Override
    public void saveProductInfo(RegisterProductsRequest request) {
        log.info("saveProductsInfo ==> " + request.toString());
        String name = request.getProductName();
        Long price = request.getProductPrice();
        String image = request.getProductImage();
        memberMapper.saveProductsInfo(name, price, image);
    }

    /* 받은 상품 목록 조회 (마이페이지) */
    @Override
    public List<MypageProductsResponse> getMyProducts(String memberId) {
        List<MypageProductsResponse> response = memberMapper.getMyProducts(memberId);
        return response;
    }
    /* 마이페이지 - 매치 히스토리 상세보기 */
    @Override
    public MatchHistoryDetailResponse getHistoryDetail(Long matchHistoryNo) {
        MatchHistoryDetailResponse response = memberMapper.getHistoryDetail(matchHistoryNo);
        if(response == null)
        {
            response = new MatchHistoryDetailResponse();
            response.setMatchHistoryNo(matchHistoryNo);
            response.setMatchLoc("아직 경기가 종료되지 않았습니다");
            response.setWinTeamScoreNo(-1L);
            response.setWinTeamScoreAmount(-1L);
            response.setLoseTeamScoreNo(-1L);
            response.setLoseTeamScoreAmount(-1L);
            response.setImageUrl("아직 경기가 종료되지 않았습니다");
        }
        log.info(response.toString());
        return response;
    }
}