package site.hclub.hyndai.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Data
public class SignInDto {
    private String username;
    private String password;
}
