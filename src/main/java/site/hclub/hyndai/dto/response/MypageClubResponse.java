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
    private String  clubInfo;   // 클럽 소개
    private String  clubLoc;
    private String  createdAt;   // 개설일
    private String  useYN;      // 개설 여부
    private String  categoryName;
}
