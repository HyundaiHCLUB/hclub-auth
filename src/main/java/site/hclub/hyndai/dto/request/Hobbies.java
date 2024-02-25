package site.hclub.hyndai.dto.request;

import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
public class Hobbies {
    private List<String> hobbies;
}
