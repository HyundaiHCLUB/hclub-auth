package site.hclub.hyndai.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberVO  implements UserDetails{
	/**
     멤버 번호 
    */
    private Long memberNo;
    /**
     멤버 아이디
    */
    private String memberId;
    /**
     멤버 사진
    */
    private String memberImage;
    /**
     관심사
    */
    private String memberInterest;
    /**
     레이팅
    */
    private Long memberRating ;
    /**
     사원 번호
    */
    private String employeeNo;
    /**
     회원 비밀번호
    */
    private String memberPw;
    /**
     관리자여부
     */
    private String adminYn ;
    /**
     권한 role
    */
    private List<String> roles = new ArrayList<>();
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    	Collection<GrantedAuthority> authorities = new ArrayList<>();
    	authorities.add(new SimpleGrantedAuthority(roles.toString()));
    	return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

	@Override
	public String getPassword() {
		return memberPw;
	}

	@Override
	public String getUsername() {
		return memberId;
	}
}

