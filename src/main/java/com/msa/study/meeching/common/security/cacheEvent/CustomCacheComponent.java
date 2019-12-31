package com.msa.study.meeching.common.security.cacheEvent;

import com.msa.study.meeching.common.security.dto.AuthorRoleManageDTO;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Component
public class CustomCacheComponent implements ApplicationListener<CustomCacheEventMessage> {

    private Map<String, List<AuthorRoleManageDTO>> authorities;

    private List<String> rolePtnList;

    public List<String> getRolePtnList() {
        return rolePtnList;
    }

    public Map<String, List<AuthorRoleManageDTO>> getAuthorities() {
        return authorities;
    }

    public List<AuthorRoleManageDTO> getAuthoritie(String key) {
        return authorities.get(key);
    }

    @Override
    public void onApplicationEvent(CustomCacheEventMessage event) {
        authorities = event.getAuthoritiesDto()
                .stream().collect(groupingBy(AuthorRoleManageDTO::getRolePtn, toList()));

        rolePtnList = new ArrayList();
        for (AuthorRoleManageDTO authorRoleManageDTO : event.getAuthoritiesDto()) {
            rolePtnList.add(authorRoleManageDTO.getRolePtn());
        }

    }
}
