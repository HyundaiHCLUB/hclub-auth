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
     * 작성자: 김동욱
     * 처리 내용: 멤버 정보를 삽입한다.
     */
    void insertMemberInfo(MemberVO mvo);

    /* 마이페이지 - 기본 인적사항*/
    MyPageInfoResponse getMypageUserInfo(String memberId);

    List<MypageClubResponse> getMypageClubInfo(String memberId);

    void updateMemberInfo(UpdateMemberInfoRequest request);

    List<MypageMatchHistoryResponse> getMypageMatchHistory(String memberId);
    /**
     * 작성자: 김동욱
     * 처리 내용: 현재 진행 중인 매치 목록을 불러옵니다.
     */
    List<MyPageProceedingMatchResponse> getMyPageProceedingMatch(String memberId);
    /**
     * 작성자: 김동욱
     * 처리 내용: 유저 관심사 기반으로 동아리를 추천해줍니다.
     */
    void insertMemberClubInterest(String memberId, String interests);

    String updateProfileImage(MultipartFile multipartFile, String memberId) throws IOException;

    // S3 에 상품 이미지 업로드
    String insertProductImage(MultipartFile multipartFile);

    // DB에 상품 정보 저장
    void saveProductInfo(RegisterProductsRequest request);

    List<MypageProductsResponse> getMyProducts(String memberId);

    MatchHistoryDetailResponse getHistoryDetail(Long matchHistoryNo);
}
