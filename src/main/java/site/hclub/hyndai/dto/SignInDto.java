package site.hclub.hyndai.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Data
public class SignInDto {
	/**
	 유저 이름
	 */
    private String username;
	/**
	 유저 비밀번호
	 */
    private String password;
}
