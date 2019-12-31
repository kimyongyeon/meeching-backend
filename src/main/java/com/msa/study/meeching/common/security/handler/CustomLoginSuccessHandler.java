package com.msa.study.meeching.common.security.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.msa.study.meeching.common.redis.RedisCrudRepository;
import com.msa.study.meeching.common.security.dto.AuthLoginInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

//@Component
@Slf4j
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${redis-session.expired.time:300000}")
    private long expiredTime;

//    @Autowired
//    RedisTemplate<String, Object> redisTemplate;

    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStratgy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response
            , Authentication authentication) throws IOException, ServletException {

        String username = request.getParameter("username");

        // session id 획득
        String sessionId = request.getSession().getId();

        // redis 세션:권한정보 저장
//        ValueOperations<String, Object> vop = redisTemplate.opsForValue();
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        vop.set(sessionId, auth);

        // TTL 주기설정, 세션:권한정보 저장
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        /**
         * {
         * 	sessionUserAuthId: ROLE_ADMIN,
         * 	sessionUserMenu: [
         * 		"M0001","M0002"
         * 	],
         * 	sessionUserId: "",
         * 	sessionUserName: "",
         * 	sessionUserDepth: ""
         * }
         */
        AuthLoginInfoDTO authLoginInfoDTO = new AuthLoginInfoDTO();
        authLoginInfoDTO.setSessionUserAuthId("ROLE_USER");
        authLoginInfoDTO.setSessionUserId("apiuserid");
        authLoginInfoDTO.setSessionUserName("apiusername");
        authLoginInfoDTO.setSessionUserDepth("개발부");
        List<String> menus = Arrays.asList("A0001", "A0002", "A0003");
        authLoginInfoDTO.setSessionUserMenu(menus);
        Gson gson = new GsonBuilder().create();
        String authLoginInfoDTOJson  = gson.toJson(authLoginInfoDTO);
        log.debug(String.format("authLoginInfoDTOJson: ", authLoginInfoDTOJson));
//        RedisCrudRepository.set(sessionId, authLoginInfoDTOJson, expiredTime, redisTemplate);

        response.setHeader("access-token", sessionId);
        response.setHeader("auth-token", auth.getAuthorities().toString());

        SavedRequest savedRequest = requestCache.getRequest(request, response);
        String defaultUrl = "/home";
        if (savedRequest != null) {
            String targetUrl = savedRequest.getRedirectUrl();
            redirectStratgy.sendRedirect(request, response, targetUrl);
        } else {
            redirectStratgy.sendRedirect(request, response, defaultUrl);
        }
    }
}
