package site.hclub.hyndai.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class JwtToken {
	/**
	  grantType
	 */
    private String grantType;
	/**
	  accessToken
	 */
    private String accessToken;
	/**
	  refreshToken
	*/
    private String refreshToken;
	/**
	 회원정보 번호
	 */
    private long memberNo;
}
