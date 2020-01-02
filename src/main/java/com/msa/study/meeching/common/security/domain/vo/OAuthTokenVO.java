package com.msa.study.meeching.common.security.domain.vo;

import lombok.Data;
import org.springframework.boot.convert.DataSizeUnit;

@Data
public class OAuthTokenVO {
    private String access_token;
    private String token_type;
    private String refresh_token;
    private long expires_in;
    private String scope;
}
