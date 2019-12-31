package com.msa.study.meeching.common.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RoleHierarchyService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> roleHierarchy() {
        // TODO: 쿼리 불러서 설정해야 함.
        String sql = "SELECT a.CHLDRN_ROLE child, a.PARNTS_ROLE parent\n" +
                "FROM COMTNROLES_HIERARCHY a \n" +
                "LEFT JOIN COMTNROLES_HIERARCHY b \n" +
                "on (a.CHLDRN_ROLE = b.PARNTS_ROLE)";
        return jdbcTemplate.queryForList(sql);
    }
}
