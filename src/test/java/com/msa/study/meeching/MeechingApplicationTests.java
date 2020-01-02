package com.msa.study.meeching;

import com.msa.study.meeching.common.security.domain.entity.UserEntity;
import com.msa.study.meeching.common.security.repository.UserJpaRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

@SpringBootTest
class MeechingApplicationTests {

   @Autowired
   private UserJpaRepo userJpaRepo;

   @Autowired
   private PasswordEncoder passwordEncoder;

   @Test
   public void insertNewUser() {
      userJpaRepo.save(UserEntity.builder()
              .uid("happydaddy@gmail.com")
              .password(passwordEncoder.encode("1234"))
              .name("happydaddy")
              .roles(Collections.singletonList("ROLE_USER"))
              .build());
   }

   @Test
   public void encodeTest() {
      System.out.printf("testSecret : %s\n", passwordEncoder.encode("testSecret"));
      /** 위에서 얻은 시크릿 암호를 넣는다.
       * insert into oauth_client_details(client_id, resource_ids,client_secret,scope,authorized_grant_types,web_server_redirect_uri,authorities,access_token_validity,refresh_token_validity,additional_information,autoapprove)
       * values('testClientId',null,'{bcrypt}$2a$10$H2oQgFY7qCRHWqkvAV4P6ONy2v74wfr3fQv.xERw3BJYSqh/Gcgrq','read,write','authorization_code,refresh_token','http://localhost:8081/oauth2/callback','ROLE_USER',36000,50000,null,null);
       *
       * 테스트 확실하게 하기 위해 다시 해본다.
       * delete from oauth_client_details;
       */
   }

   /**
    * 비밀번호암호화
    */
   @Test
   public void 비밀번호_암호화() {
      BCryptPasswordEncoder bcr = new BCryptPasswordEncoder();
      String result = bcr.encode("1234");
      System.out.println("암호 === " + result);
   }

   @Test
   void contextLoads() {
   }

}
