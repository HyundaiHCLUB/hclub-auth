package site.hclub.hyndai.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import site.hclub.hyndai.service.JwtTokenProvider;
import site.hclub.hyndai.service.UserService;

/**
 * @author 김은솔
 * @description: Security Config 설정
 * ===========================
	   AUTHOR      NOTE
 * ---------------------------
 *     김은솔       최초생성
 * ===========================
 */
@Configuration
@ComponentScan("site.hclub.hyndai")
@PropertySource("classpath:application.yml")
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthenticationConfig {
	
	private final UserService userService;
	
	private String secretKey;
	@Value("${jwt-secret}")
	
	public void setSecretKey(String secretKey) {
	    this.secretKey = secretKey;
	}
	private final JwtTokenProvider jwtTokenProvider;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity)throws Exception{
		return httpSecurity
				.httpBasic().disable()
				.csrf().disable()
				.cors()
				.and()
				.authorizeRequests()
				.antMatchers("/").permitAll()
				.and()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)// jwt 사용하는 경우
				.and()
				.addFilterBefore(new JwtFilter(userService, secretKey, jwtTokenProvider) , UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	 @Bean
	 public static PasswordEncoder passwordEncoder() {
	       return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	 }
}
