package site.hclub.hyndai.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
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
    
    private long tokenId; 
}
