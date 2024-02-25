package site.hclub.hyndai.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.SpringApplication;

@Getter
@Setter
@ToString
public class MypageClubResponse {
    private String  clubName;
    private String  clubImage;
    private String  clubInfo;
    private String  clubLoc;
    private String  categoryName;
}
