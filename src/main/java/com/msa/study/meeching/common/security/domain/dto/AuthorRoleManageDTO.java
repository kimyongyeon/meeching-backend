package com.msa.study.meeching.common.security.domain.dto;

import lombok.Data;

@Data
public class AuthorRoleManageDTO {
    private static final long serialVersionUID = 1L;

    private String authId;
    private String authCode;
    private String roleId;
    private String roleNm;
    private String rolePtn;
    private String roleDesc;
    private String roleTypCd;
}
