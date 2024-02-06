package site.hclub.hyndai.sample.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.log4j.Log4j;
import site.hclub.hyndai.common.base.dto.ResponseSuccessDTO;
import site.hclub.hyndai.common.respone.dto.SampleResponseDTO;
import site.hclub.hyndai.sample.domain.SampleVO;
import site.hclub.hyndai.sample.service.SampleService;

@RestController
@RequestMapping("/sample")
@Log4j
public class SampleController {
    
    @Autowired
    SampleService sampleService;

    @RequestMapping("/list")
    public ResponseEntity<List<SampleVO>> getSampleList(SampleVO svo){
         
        return ResponseEntity.ok(sampleService.getSampleList(svo));
    }
}
