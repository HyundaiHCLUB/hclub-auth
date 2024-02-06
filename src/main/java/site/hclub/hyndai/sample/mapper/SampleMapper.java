package site.hclub.hyndai.sample.mapper;

import java.util.List;

import site.hclub.hyndai.sample.domain.SampleVO;

public interface SampleMapper {

	List<SampleVO> getSampleList(SampleVO svo);
}
