package site.hclub.hyndai.mapper;

import site.hclub.hyndai.domain.Employee;
import site.hclub.hyndai.domain.MemberVO;
import site.hclub.hyndai.dto.EmployeeDTO;

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
}
