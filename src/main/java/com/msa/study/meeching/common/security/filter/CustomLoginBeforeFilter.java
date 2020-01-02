package com.msa.study.meeching.common.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class CustomLoginBeforeFilter extends GenericFilterBean {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        try {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String user_id = auth.getName();

            if ("anonymousUser".equals(user_id)) {
                httpServletResponse.sendRedirect("/login");
            } else {
                chain.doFilter(httpServletRequest, httpServletResponse);
            }
        } catch(Exception e) {
            log.error(this.getClass().getName(), e);
        }
    }
}
