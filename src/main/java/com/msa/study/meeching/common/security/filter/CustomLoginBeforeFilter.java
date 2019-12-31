package com.msa.study.meeching.common.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@Component
@Slf4j
public class CustomLoginBeforeFilter extends GenericFilterBean {
//    @Autowired
//    RedisTemplate<String, Object> redisTemplate;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        try {
            /**
             * 헤더에 있는 sessionToken을 받아서
             * 레디스 저장소에서 해당 키에 대한 값이있는 조회 하고,
             * 존재하면 메인페이지로 이동을 위해 sessionToken을 설정해서 filter로 내려준다.
             */
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            chain.doFilter(httpServletRequest, httpServletResponse);
//            String accessToken = httpServletRequest.getHeader("access-token");
//            String redisSessionToken = RedisCrudRepository.get(accessToken, redisTemplate);
//            if (StringUtils.isEmpty(redisSessionToken)) {
//                // 로그인 페이지 이동
//                log.debug("redisSessionToken="+redisSessionToken);
//                httpServletResponse.setHeader("access-token", "");
//                chain.doFilter(httpServletRequest,httpServletResponse);
//            } else {
//                // 메인 페이지 이동
//                log.debug("redisSessionToken="+redisSessionToken);
//                httpServletResponse.setHeader("access-token", redisSessionToken);
//                chain.doFilter(httpServletRequest,httpServletResponse);
//            }

        } catch(Exception e) {
            log.error(this.getClass().getName(), e);
        }
    }
}
