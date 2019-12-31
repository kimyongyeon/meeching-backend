package com.msa.study.meeching.common.security;

import com.msa.study.meeching.common.security.filter.CustomLoginBeforeFilter;
import com.msa.study.meeching.common.security.filter.MetadataSourceFilter;
import com.msa.study.meeching.common.security.handler.CustomLoginSuccessHandler;
import com.msa.study.meeching.common.security.handler.CustomLogoutSuccessHandler;
import com.msa.study.meeching.common.security.provider.CustomAuthenticationProvider;
import com.msa.study.meeching.common.security.service.RoleHierarchyService;
import com.msa.study.meeching.common.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private Environment environment;

    @Autowired
    private CustomLoginSuccessHandler customLoginSuccessHandler;

    @Autowired
    private CustomLogoutSuccessHandler customLogoutSuccessHandler;

    @Autowired
    RoleHierarchyService roleHierarchyService;

    @Autowired
    private CustomLoginBeforeFilter customLoginBeforeFilter;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MetadataSourceFilter metadataSourceFilter;


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public FilterSecurityInterceptor filterSecurityInterceptor() {
        FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();
        filterSecurityInterceptor.setAuthenticationManager(authenticationManager);
        filterSecurityInterceptor.setSecurityMetadataSource(metadataSourceFilter);
        filterSecurityInterceptor.setAccessDecisionManager(affirmativeBased());
        return filterSecurityInterceptor;
    }

    @Bean
    public AffirmativeBased affirmativeBased() {
        List<AccessDecisionVoter<? extends Object>> accessDecisionVoters = new ArrayList<>();
        accessDecisionVoters.add(roleVoter());
        AffirmativeBased affirmativeBased = new AffirmativeBased(accessDecisionVoters);
        return affirmativeBased;
    }

    @Bean
    public RoleHierarchyVoter roleVoter() {
        RoleHierarchyVoter roleHierarchyVoter = new RoleHierarchyVoter(roleHierarchy());
        roleHierarchyVoter.setRolePrefix("ROLE_");
        return roleHierarchyVoter;
    }

//    RoleHierarchy 설정
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        StringBuilder stringBuilder = new StringBuilder();
//        List<Map<String, Object>> list = roleHierarchyService.roleHierarchy();
//        list.stream()
//                .forEach(i -> {
//                    stringBuilder.append(i.get("child"));
//                    stringBuilder.append(" > ");
//                    stringBuilder.append(i.get("parent"));
//                    stringBuilder.append("\n");
//                });
        roleHierarchy.setHierarchy(stringBuilder.toString());
        return roleHierarchy;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        CustomAuthenticationProvider authenticationProvider = getAuthenticationProvider(userService);
        auth.authenticationProvider(authenticationProvider);
    }

    @Bean
    public CustomAuthenticationProvider getAuthenticationProvider(UserService userService) {
        CustomAuthenticationProvider authenticationProvider = new CustomAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    WebContentInterceptor webContentInterceptor() { // 로그인 후 백버튼 방지
        WebContentInterceptor webContentInterceptor = new WebContentInterceptor();
        webContentInterceptor.setCacheSeconds(0);
        return webContentInterceptor;
    }

    /**
     * Authorization with Roles
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        log.info("▶ BrowserSecurityConfig configure");

//    	http.addFilterBefore(customLoginBeforeFilter, UsernamePasswordAuthenticationFilter.class); // 세션체크
//        disableSecurityForLocalH2Console(http);

        // post, put 허용 하기 위한 조치
        http.csrf().disable();

        http
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/").permitAll();

//        http
//                .antMatcher("/**")
//                .authorizeRequests()
//                .antMatchers("/").permitAll()
//                .antMatchers("/login").permitAll()
//                .antMatchers("/boa/**").hasAnyRole("ROLE_ADMIN")
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginPage("/login").permitAll()
//                .defaultSuccessUrl("/home", true)
//                .failureUrl("/login?error=true")
//                .usernameParameter("loginId")
//                .passwordParameter("password")
//                .successHandler(customLoginSuccessHandler)
//                .and()
//                .addFilter(filterSecurityInterceptor()) // 권한
//                .logout()
//                .logoutUrl("/logout")
//                .logoutSuccessHandler(customLogoutSuccessHandler)
//                .logoutSuccessUrl("/login")
//                .invalidateHttpSession(true)
//                .deleteCookies("JSESSIONID")
//        ;
    }

    /**
     * Ignore static resources.
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**");
    }

    /**
     * Local용 H2 DB web console에 대한, Security 설정 해제
     *
     * @param http
     * @throws Exception
     */
    private void disableSecurityForLocalH2Console(HttpSecurity http) throws Exception {
        if (isLocalProfile()) {
            log.warn("Disable security to allow H2 console");
            String url = "/h2-console/**";
            http.csrf().ignoringAntMatchers(url);
            http.authorizeRequests().antMatchers(url).permitAll();
            http.headers().frameOptions().disable();
        }
    }

    /**
     * Spring Profile이 로컬(local)인지 Check.
     *
     * @return
     */
    private boolean isLocalProfile() {
        boolean isLocal = Arrays.asList(environment.getActiveProfiles()).contains("local");
        log.debug("▶ isLocal : {}", isLocal);
        return isLocal;
    }
}
