package site.hclub.hyndai.common.response;
import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SuccessType {
    /**
     * 200 Ok
     */
	
	GET_MEMBER_DETAIL_SUCCESS(HttpStatus.OK, "멤버 정보 조회 성공"),
	INSERT_MEMBER_INFO_SUCCESS(HttpStatus.OK, "멤버 정보 삽입 성공"),
	GET_EMPLOYEE_YN_SUCCESS(HttpStatus.OK, "직원여부 조회 성공")
    /**
     * 201 Created
     */

    ;
    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
