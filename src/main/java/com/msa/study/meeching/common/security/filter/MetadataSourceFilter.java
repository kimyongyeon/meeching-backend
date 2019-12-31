package com.msa.study.meeching.common.security.filter;

import com.msa.study.meeching.common.security.cacheEvent.CustomCacheComponent;
import com.msa.study.meeching.common.security.dto.AuthorRoleManageDTO;
import com.msa.study.meeching.common.security.service.SecuredObjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.AntPathMatcher;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
//@Service
public class MetadataSourceFilter implements FilterInvocationSecurityMetadataSource, InitializingBean {

    @Autowired
    private CustomCacheComponent cacheManager;
    private Map<RequestMatcher, List<ConfigAttribute>> requestMap;

//    @Autowired
//    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private SecuredObjectService securedObjectService;

    /**
     * ApiSecurityConfig에서 addFilter로 등록되어 호출되는 메소드
     * 요청한 request url에 대한 ROLE_ 이름을 리턴한다.
     * @param object
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation fi = (FilterInvocation) object;
        String url = fi.getRequestUrl();

        boolean ptnUrlChk = false;
        String dbUrl = "";
        for (String ptnUrl : cacheManager.getRolePtnList()) {
            if (new AntPathMatcher().match(ptnUrl, url)) {
                ptnUrlChk = true;
                dbUrl = ptnUrl;
            }
        }
        if (!ptnUrlChk) {
            return null;
        }
        List<AuthorRoleManageDTO> userRoleDtos = cacheManager.getAuthorities().get(dbUrl);
        if (userRoleDtos == null) {
            return null;
        }
        List<String> roles = userRoleDtos.stream().map(AuthorRoleManageDTO::getAuthId).collect(Collectors.toList());
        String[] stockArr = new String[roles.size()];
        stockArr = roles.toArray(stockArr);
        return SecurityConfig.createList(stockArr);
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Set<ConfigAttribute> allAttributes = new HashSet<ConfigAttribute>();
        if (requestMap != null) {
            for(Map.Entry<RequestMatcher, List<ConfigAttribute>> entry : requestMap.entrySet()) {
                allAttributes.addAll(entry.getValue());
            }
        }
        return allAttributes;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

    /**
     * 권한설정 실시간 반영을 위한 메소드
     * @throws Exception
     */
    public void reload() throws Exception{
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> reloadedMap = securedObjectService.findRolesAndUrl();
        Iterator<Map.Entry<RequestMatcher, List<ConfigAttribute>>> iterator = reloadedMap.entrySet().iterator();
        //이전 데이터 삭제
        if (requestMap != null) {
            requestMap.clear();
            while(iterator.hasNext()) {
                Map.Entry<RequestMatcher, List<ConfigAttribute>> entry = iterator.next();
                requestMap.put(entry.getKey(), entry.getValue());
            }
        } else {
            requestMap = reloadedMap;
        }
        log.info("Secured Url Resources - Role Mappings reloaded at Runtime!!");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // todo: mapper to jpa 수정후 살려야 함.
//        requestMap = securedObjectService.findRolesAndUrl();
    }
}
