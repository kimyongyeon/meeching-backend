package com.msa.study.meeching.common.security.handler;

import com.msa.study.meeching.common.redis.RedisCrudRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

//    @Autowired
//    RedisTemplate<String, Object> redisTemplate;

    @Value("${redis-session.expired.time:300000}")
    private long expiredTime;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        if (authentication != null && authentication.getDetails() != null) {
            try {
                request.getSession().invalidate();
                String sessionId = request.getRequestedSessionId();
//                RedisCrudRepository.set(sessionId, "", expiredTime, redisTemplate);
//                RedisCrudRepository.set("access-token", "", expiredTime, redisTemplate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect("/");

    }
}
