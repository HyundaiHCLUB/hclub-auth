package site.hclub.hyndai.mapper;

import org.apache.ibatis.annotations.Param;
import site.hclub.hyndai.domain.Employee;
import site.hclub.hyndai.domain.MemberVO;
import site.hclub.hyndai.dto.EmployeeDTO;
import site.hclub.hyndai.dto.request.RegisterProductsRequest;
import site.hclub.hyndai.dto.request.UpdateMemberInfoRequest;
import site.hclub.hyndai.dto.response.MypageClubResponse;
import site.hclub.hyndai.dto.response.MypageMatchHistoryResponse;

import java.util.List;

/**
 * @author 김은솔
 * @description: 멤버 관련 Mapper 인터페이스를 작성한다.
 * ===========================
	   AUTHOR      NOTE
 * ---------------------------
 *    김은솔        최초생성
 * ===========================
 */
public interface MemberMapper {

	/**
	 작성자: 김은솔 
	 처리 내용: 멤버 아이디를 기준으로 멤버 정보를 조회한다.
	*/
	MemberVO getMemberInfo(String userId);
	
	MemberVO getMemberInfoByVo(MemberVO mvo);
	
	/**
	 작성자: 김은솔 
	 처리 내용: 멤버 아이디를 기준으로 해당 아이디가 존재하는지를 조회한다.
	*/
	int getMemberIdYn(String userId);
	
	/**
	 작성자: 김은솔 
	 처리 내용: 사원번호를 기준으로 해당 사원이 존재하는지를 조회한다.
	*/
	int getEmployeeYn(EmployeeDTO dto);
	/**
	 작성자: 김은솔 
	 처리 내용: 멤버정보를 바탕으로 회원가입한다.
	*/
	int insertMemberInfo(MemberVO mvo);

	/* input : 사원번호 -> output: employee */
    Employee getEmployeeInfo(String employeeNo);

	List<MypageClubResponse> getMypageClubInfo(String memberId);

	void updateUserPw(UpdateMemberInfoRequest request);

	List<MypageMatchHistoryResponse> getMypageMatchHistory(String memberId);

	void updateProfileImage(@Param("url") String url, @Param("memberId") String memberId);

	void saveProductsInfo(@Param("name")String name, @Param("price") Long price, @Param("image")String image);
}
