package com.mycom.app.user.service;

import com.mycom.app.user.entity.SiteUser;
import com.mycom.app.user.repository.UserRepository;
import com.mycom.app.user.validation.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//spring security에 등록할 클래스
@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {

    private final UserRepository userRepository;

    //사용자이름으로 (비밀번호)조회
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //제시한 username을 가진 사용자가 존재하는 지 체크
        Optional<SiteUser> siteUser = userRepository.findByusername(username);
        if(siteUser.isEmpty()){
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        //제시한 username을 가진 사용자가 존재하는 경우 아래 코드 진행
        SiteUser siteUser1 = siteUser.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        //username이 "admin"이라면
        if ("admin".equals(username)) {  //"ROLE_ADMIN"을 권한목록추가=>ADMIN권한부여
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        } else { //그외의 경우 "ROLE_USER"을 권한목록추가=>USER권한부여
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }
        //사용자User객체리턴
        return new User(siteUser1.getUsername(), siteUser1.getPassword(), authorities);
        //스크링시큐리티는  loadUserByUsername()에 의해 리턴되는 User객체의 비밀번호가
        // 화면으로부터 입력한 비번와 일치하는지 검사하는 로직이 내부적으로 존재
    }
}
/* 교재 653참고
UserDetails - spring security에서 사용자의 정보를 담는 인터페이스
UserDetailsService- spring security에서 사용자의 정보를 가져오는 인터페이스

getAutorities() : Collection<? extends GrantedAuthority>=>계정의 권한 목록을 리턴	-
getPassword() : String	계정의 비밀번호를 리턴
getUsername() : String	계정의 고유한 값을 리턴 (DB PK 값, 중복이 없는 email)	-
isAccountNonExpired() : boolean	계정의 만료 여부 리턴=>기본값은 true(만료 안됨)
isAccountNonLocked() : boolean	계정의 잠김 여부 리턴=>기본값은 true (잠기지 않음)
isCredentialsNonExpired() : boolean 비밀번호 만료 여부 리턴=>기본값은 true(만료 안됨)
*/