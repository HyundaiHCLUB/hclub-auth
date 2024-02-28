package site.hclub.hyndai.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MypageMatchHistoryResponse {
    private Long    teamNo;             // 팀 번호
    private String  matchType;          // 종목
    private String  teamName;           // 팀이름
    private Long    teamRating;         // 팀레이팅
    private String  matchLoc;           // 경기장소
    private String  matchDate;          // 경기 일자
    private Long    matchCapacity;
    private Long    score1;
    private Long    score2;
}
