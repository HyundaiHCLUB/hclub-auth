package site.hclub.hyndai.dto.response;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
public class HobbiesClassifiedResponse {
    private List<Integer> hobbies;
}
