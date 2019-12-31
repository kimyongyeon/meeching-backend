package com.msa.study.meeching.common.security.dto;

import lombok.Data;

import java.util.List;

@Data
public class AuthLoginInfoDTO {
    private String sessionUserAuthId;
    private List<String> sessionUserMenu;
    private String sessionUserId;
    private String sessionUserName;
    private String sessionUserDepth;
}
