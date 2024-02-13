package site.hclub.hyndai.mapper;

import site.hclub.hyndai.domain.JwtToken;

public interface TokenMapper {

	int insertTokenMemberInfo(JwtToken jwtToken);
}
