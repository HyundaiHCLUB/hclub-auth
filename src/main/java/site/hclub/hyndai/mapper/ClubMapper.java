package site.hclub.hyndai.mapper;

import org.apache.ibatis.annotations.Param;

public interface ClubMapper {

    // 카테고리별 인기 동아리를 가져옵니다.
    Long getClubByCategory(int categoryNo);
    // 유저 추천동아리를 추가해줍니다.
    int insertMemberClubInterest(@Param("memberNo") Long memberNo, @Param("clubNo") Long clubNo);
}
