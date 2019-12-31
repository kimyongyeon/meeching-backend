package com.msa.study.meeching;

import com.msa.study.meeching.websocket.CommonWebSocket;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MeechingApplication {

   // 인사이트 = 통찰력
   public static void main(String[] args)
   {
      // websocket 구동
      CommonWebSocket commonWebSocket = new CommonWebSocket();
      commonWebSocket.init();
      // springboot 구동
      SpringApplication.run(MeechingApplication.class, args);
   }

}
