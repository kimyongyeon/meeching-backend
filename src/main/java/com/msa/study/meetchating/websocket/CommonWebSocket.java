package com.msa.study.meetchating.websocket;

import io.javalin.Javalin;
import lombok.extern.slf4j.Slf4j;

// endpoint = gateway -> 7000 (websocket), 8080(http)

@Slf4j
public class CommonWebSocket {

   public void init () {
      Javalin app = Javalin.create().start(7000);
      app.ws("/chat", ws -> {
         ws.onConnect(ctx -> log.debug("Connected"));
         ws.onMessage(ctx -> {
            UserVO user = ctx.message(UserVO.class); // convert from json
            ctx.send(user); // convert to json and send back
         });
         ws.onBinaryMessage(ctx -> log.debug("Message"));
         ws.onClose(ctx -> log.debug("Closed"));
         ws.onError(ctx -> log.debug("Errored"));
      });
   }
}
