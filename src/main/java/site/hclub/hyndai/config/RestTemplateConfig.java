package site.hclub.hyndai.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
/**
 * @author 김동욱
 * @description: Redis Config File
 * ===========================
AUTHOR      NOTE
 * ---------------------------
 *    김동욱        최초생성
 * ===========================
 */
@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
