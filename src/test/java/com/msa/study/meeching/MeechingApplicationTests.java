package com.msa.study.meeching;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//@SpringBootTest
class MeechingApplicationTests {

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
