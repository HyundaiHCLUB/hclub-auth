package site.hclub.hyndai.mapper;

import site.hclub.hyndai.domain.MemberVO;

public interface MemberMapper {
	
	MemberVO getMemberInfo(String userId);

}
