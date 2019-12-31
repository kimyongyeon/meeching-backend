package com.msa.study.meeching.common.security.service;

import com.msa.study.meeching.common.security.dto.AuthorRoleManageDTO;
import com.msa.study.meeching.common.security.cacheEvent.CustomCacheEventMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service("SecuredObjectService")
public class SecuredObjectService {

    @Autowired
    private ApplicationContext applicationContext;

    public List<AuthorRoleManageDTO> selectAuthorAllRoleList(AuthorRoleManageDTO authorRoleManageDTO) throws Exception {
        return null;
    }

    public LinkedHashMap<RequestMatcher, List<ConfigAttribute>> findRolesAndUrl() throws Exception {
        LinkedHashMap<RequestMatcher, List<ConfigAttribute>> ret = new LinkedHashMap<RequestMatcher, List<ConfigAttribute>>();
        LinkedHashMap<Object, List<ConfigAttribute>> data = getRolesAndUrl();
        Set<Object> keys = data.keySet();
        for(Object key : keys) {
            ret.put((AntPathRequestMatcher)key, data.get(key));
        }
        return ret;
    }

    public LinkedHashMap<Object, List<ConfigAttribute>> getRolesAndUrl() throws Exception{
        return getRolesAndResources("url");
    }

    public LinkedHashMap<Object, List<ConfigAttribute>> getRolesAndMethod() throws Exception{
        return getRolesAndResources("method");
    }

    public LinkedHashMap<Object, List<ConfigAttribute>> getRolesAndPointcut() throws Exception{
        return getRolesAndResources("pointcut");
    }

    public LinkedHashMap<Object, List<ConfigAttribute>> getRolesAndResources(String resourceType) throws Exception {
        LinkedHashMap<Object, List<ConfigAttribute>> resourcesMap = new LinkedHashMap<Object, List<ConfigAttribute>>();

        String sqlRolesAndResources = null;
        boolean isResourcesUrl = true;
        List<AuthorRoleManageDTO> resultList = null;
        if("method".equals(resourceType)) {
            //sqlRolesAndResources = getSqlRolesAndMethod();
            //isResourcesUrl = false;
        }else if("pointcut".equals(resourceType)) {
            //sqlRolesAndResources = getSqlRolesAndPointcut();
            //isResourcesUrl = false;
        }else {
            resultList = selectAuthorAllRoleList(new AuthorRoleManageDTO());
            resultList.stream().forEach(userRoleDto -> {
                log.info("role name {} ", userRoleDto.getRoleNm());
                log.info("url {}", userRoleDto.getRolePtn());
            });
            applicationContext.publishEvent(new CustomCacheEventMessage(this, resultList));
        }

        Iterator<AuthorRoleManageDTO> itr = resultList.iterator();
        AuthorRoleManageDTO tempMap;
        String preResource = null;
        String presentResourceStr;
        Object presentResource;

        while(itr.hasNext()) {
            tempMap = itr.next();

            presentResourceStr = (String) tempMap.getRoleTypCd();
            presentResource = isResourcesUrl ? new AntPathRequestMatcher(presentResourceStr): presentResourceStr;
            List<ConfigAttribute> configList = new LinkedList<ConfigAttribute>();

            if(preResource != null && presentResourceStr.equals(presentResource)) {
                List<ConfigAttribute> preAuthList = resourcesMap.get(presentResource);
                Iterator<ConfigAttribute> preAuthItr = preAuthList.iterator();
                while(preAuthItr.hasNext()) {
                    SecurityConfig tempConfig = (SecurityConfig) preAuthItr.next();
                    configList.add(tempConfig);
                }
            }

            configList.add(new SecurityConfig((String) tempMap.getAuthCode()));
//            configList.add(new SecurityConfig((String) "ROLE_ADMIN"));

            resourcesMap.put(presentResource, configList);

            //이전 resource 비교 위해 저장
            preResource = presentResourceStr;
        }

        return resourcesMap;
    }
}
