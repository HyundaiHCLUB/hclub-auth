package site.hclub.hyndai.service;

import org.springframework.web.multipart.MultipartFile;
import site.hclub.hyndai.domain.JwtToken;
import site.hclub.hyndai.domain.MemberVO;
import site.hclub.hyndai.dto.EmployeeDTO;
import site.hclub.hyndai.dto.request.RegisterProductsRequest;
import site.hclub.hyndai.dto.request.UpdateMemberInfoRequest;
import site.hclub.hyndai.dto.response.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public interface MemberService {

    /**
     * 작성자: 김은솔
     * 처리 내용: 발급된 JwtToken과 userId를 Token테이블에 isnert한다.
     */
    int insertTokenInfo(JwtToken jwtToken, String userId);

    /**
     * 작성자: 김은솔
     * 처리 내용: 멤버 정보를 확인한다.
     */
    MemberVO getMemberInfo(String userId);

    /**
     * 작성자: 김은솔
     * 처리 내용: 직원여부를 조회한다.
     */
    String getEmployeeYn(EmployeeDTO dto);

    /**
     * 작성자: 김은솔
     * 처리 내용: Header에 담긴 token정보를 resolve한다.
     */
    String resolveToken(HttpServletRequest request);

    /**
     * 작성자: 김은솔
     * 처리 내용: 회원 정보를 바탕으로 Member테이블에 삽입한다.
     */
    void insertMemberInfo(MemberVO mvo);

    /**
     * 작성자: 송원선
     * 처리 내용: 마이페이지 - 기본 인적사항
     */
    MyPageInfoResponse getMypageUserInfo(String memberId);

    /**
     * 작성자: 송원선
     * 처리 내용: <내모임> 목록
     */
    List<MypageClubResponse> getMypageClubInfo(String memberId);

    /**
     * 작성자: 송원선
     * 처리 내용: 회원정보수정
     */
    void updateMemberInfo(UpdateMemberInfoRequest request);

    /**
     * 작성자: 송원선
     * 처리 내용: 마이페이지 - 매치히스토리
     */
    List<MypageMatchHistoryResponse> getMypageMatchHistory(String memberId);

    /**
     * 작성자: 김동욱
     * 처리 내용: 마이페이지 - 진행중인 매치
     */
    List<MyPageProceedingMatchResponse> getMyPageProceedingMatch(String memberId);

    /**
     * 작성자: 김동욱
     * 처리 내용: 회원 관심사 저장
     */
    void insertMemberClubInterest(String memberId, String interests);

    /**
     * 작성자: 송원선
     * 처리 내용: 프로필 이미지 수정
     */
    String updateProfileImage(MultipartFile multipartFile, String memberId) throws IOException;

    /**
     * 작성자: 송원선
     * 처리 내용: S3 상품 이미지 업로드
     */
    String insertProductImage(MultipartFile multipartFile);

    /**
     * 작성자: 송원선
     * 처리 내용: 상품정보 DB 저장
     */
    void saveProductInfo(RegisterProductsRequest request);

    /**
     * 작성자: 송원선
     * 처리 내용: 마이페이지 - 내 선물함
     */
    List<MypageProductsResponse> getMyProducts(String memberId);

    /**
     * 작성자: 송원선
     * 처리 내용: 마이페이지 - 매치히스토리 - 상세보기
     */
    MatchHistoryDetailResponse getHistoryDetail(Long matchHistoryNo);
}
