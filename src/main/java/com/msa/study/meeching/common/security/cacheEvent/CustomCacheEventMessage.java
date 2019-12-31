package com.msa.study.meeching.common.security.cacheEvent;

import com.msa.study.meeching.common.security.domain.dto.AuthorRoleManageDTO;
import org.springframework.context.ApplicationEvent;

import java.util.List;

public class CustomCacheEventMessage extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    final List<AuthorRoleManageDTO> authoritiesDto;

    public CustomCacheEventMessage(Object source, final List<AuthorRoleManageDTO> authoritiesDto) {
        super(source);
        this.authoritiesDto = authoritiesDto;
    }

    public List<AuthorRoleManageDTO> getAuthoritiesDto() {
        return authoritiesDto;
    }
}
