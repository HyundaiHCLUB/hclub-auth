package site.hclub.hyndai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class MyPageProceedingMatchResponse {
    private Long matchNo;
    private String gameType;
    private String myTeamName;
    private String matchCapacity;
    private String matchLoc;
    private String matchDate;
    private String opponentTeamName;

}
