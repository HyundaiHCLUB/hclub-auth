package site.hclub.hyndai.mapper;

import org.apache.ibatis.annotations.Param;

public interface ClubMapper {

    /**
     * 작성자: 김동욱
     * 처리 내용: 해당 카테고리에 해당하는 동아리를 불러옵니다.
     */
    Long getClubByCategory(int categoryNo);

    /**
     * 작성자: 김동욱
     * 처리 내용: 유저에 해당하는 추천 동아리 삽입해줍니다.
     */
    int insertMemberClubInterest(@Param("memberNo") Long memberNo, @Param("clubNo") Long clubNo);
}
