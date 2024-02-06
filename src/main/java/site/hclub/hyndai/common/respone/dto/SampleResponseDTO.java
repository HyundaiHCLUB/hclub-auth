package site.hclub.hyndai.common.respone.dto;
import site.hclub.hyndai.sample.domain.SampleVO;
import java.util.List;

import lombok.Data;


@Data
public class SampleResponseDTO {
	private List<SampleVO> sampleList;
}
