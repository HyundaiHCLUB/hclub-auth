package site.hclub.hyndai.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MyPageInfoResponse {
    // from 'employee' table
    private String  employeeName;   // 이름
    private String  employeeDept;   // 부서명
    private String  employeePosition; // 직급
    // from 'member' table
    private String  member_id;      // 아이디
    private String  memberImage;    // 사진
    private String  memberInterest; // 흥미
    private Long    memberRating;   // 레이팅

}
