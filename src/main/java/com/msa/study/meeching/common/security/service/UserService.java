package com.msa.study.meeching.common.security.service;

import com.msa.study.meeching.common.security.domain.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {

        log.debug("[loadUserByUsername] username => {}", username);
        if ("fail".equals(username)) {
            throw new UsernameNotFoundException("사용자[" + username + "] 인증에 실패하였습니다.");
        }
        //인증된 사용자 반환 --------------------
        return UserEntity
                .builder()
                .name(username)
                .password(passwordEncoder.encode("1234"))
                .roles(Collections.singletonList("ROLE_USER")).build();
    }
}
