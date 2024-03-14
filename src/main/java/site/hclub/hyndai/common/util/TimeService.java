package site.hclub.hyndai.common.util;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;
/**
 * @author 김동욱
 * @description: String <-> LocalDateTime
 * ===========================
AUTHOR      NOTE
 * ---------------------------
 *    김동욱        최초생성
 * ===========================
 */
@Service
public class TimeService {



    public String parseLocalDateTime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDateTime.format(formatter);
    }

    

    
}
