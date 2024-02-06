package site.hclub.hyndai.sample.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.hclub.hyndai.sample.domain.SampleVO;
import site.hclub.hyndai.sample.mapper.SampleMapper;


@Service
public class SampleServiceImpl implements SampleService{
	
	@Autowired
	SampleMapper sampleMapper;

	@Override
	public List<SampleVO> getSampleList(SampleVO svo) {
	
		return sampleMapper.getSampleList(svo);
	}

}
