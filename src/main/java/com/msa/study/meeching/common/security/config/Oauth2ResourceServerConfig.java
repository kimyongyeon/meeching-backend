package com.msa.study.meeching.common.security.config;

import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

//@Configuration
//@EnableResourceServer
public class Oauth2ResourceServerConfig extends ResourceServerConfigurerAdapter {


//    @Value("${security.oauth2.jwt.signkey}")
//    private String signKey;
//
//    @Bean
//    public TokenStore tokenStore() {
//        return new JwtTokenStore(accessTokenConverter());
//    }
//
//    @Bean
//    public JwtAccessTokenConverter accessTokenConverter() {
//        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//        converter.setSigningKey(signKey);
//        return converter;
//    }
//
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http.headers().frameOptions().disable();
//        http.authorizeRequests()
//                .antMatchers("/member/**").access("#oauth2.hasScope('read')")
//                .anyRequest().authenticated();
//    }
}
