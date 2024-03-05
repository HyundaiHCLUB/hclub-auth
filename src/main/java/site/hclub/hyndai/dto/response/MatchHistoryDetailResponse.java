package site.hclub.hyndai.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MatchHistoryDetailResponse {
    // FROM MATCH TABLE
    private Long    matchHistoryNo;
    private String  matchLoc;
    private Long    winTeamScoreNo;
    private Long    loseTeamScoreNo;
    private String  matchDate;
    // FROM SCORE TABLE
    private Long    winTeamScoreAmount;
    private Long    loseTeamScoreAmount;
    // FROM IMAGE TABLE
    private String  imageUrl;
}
