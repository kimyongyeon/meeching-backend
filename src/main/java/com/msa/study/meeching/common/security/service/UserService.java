package com.msa.study.meeching.common.security.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class UserService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("◑ [loadUserByUsername]  username => {}", username);
        //사용자 인증 --------------------
        //TODO SSO-Agent를 통한 인증. 제공될 Library를 이용하여 인증 모듈 호출.

        //TODO 인증실패시 처리
        //  (임시) 실패계정: fail
        if ("fail".equals(username)) {
            throw new UsernameNotFoundException("사용자[" + username + "] 인증에 실패하였습니다.");
        }
        //TODO 삭제필요. 테스트 용도.
        // [주의] User 반환시 password 일 수 없음.
        // => [구현시점 기준] 사용자테이블이  없음으로, 비밀번호를 가져올 수 없음.
        // => 향후 Agent로부터 password 획득하던가 해야함.
        String password = "PASSWORD";
        //사용자별 Role 조회 및 설정 --------------------
        @SuppressWarnings("unchecked")
        Set<GrantedAuthority> authorities = (Set<GrantedAuthority>) getAuthorities(username);
        //인증된 사용자 반환 --------------------
        return new User(username, password, authorities);
    }

    /**
     * 사용자별 권한 설정.
     *
     * @param username
     * @return
     */
    private Collection<? extends GrantedAuthority> getAuthorities(String username) {

        List<String> roles = getUserRoles(username);

        log.debug("◑◑◑◑◑◑◑◑◑◑◑◑ [getAuthorities] ");
        if (roles == null || roles.isEmpty()) {
            log.debug("◑ [getAuthorities]  roles is null or empty.");
            return null;
        }

        return getGrantedAuthorities(roles);
    }

    /**
     * [사용자보안설정-COMTNEMPLYRSCRTYESTBS]테이블을 조회하여, 사용자별 설정된 Role 정보 획득
     *
     * @param username
     * @return
     */
    private List<String> getUserRoles(String username) {

        try {
//            AuthorGroupDTO searchVo = new AuthorGroupDTO();
//            searchVo.setSearchKeyword(username);
//            List<String> roles = new ArrayList<String>();
//            List<AuthorGroupDTO> list = authorGroupService.selectAuthorGroupList(searchVo);
//            list.stream().forEach(vo -> roles.add(vo.getAuthorCode()));
            // todo: 향후 디비에서 받아서 처리 해야 함.
            List<String> roles = Arrays.asList("ROLE_USER");
            return roles;
        } catch (Exception e) {
            log.error("ERROR", e);
            return null;
        }
    }

    public static Set<GrantedAuthority> getGrantedAuthorities(List<String> roles) {

        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();

        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
}
