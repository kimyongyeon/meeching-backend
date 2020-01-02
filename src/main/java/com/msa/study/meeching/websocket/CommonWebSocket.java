package com.msa.study.meeching.websocket;

import io.javalin.Javalin;
import io.javalin.websocket.WsContext;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static j2html.TagCreator.*;

// endpoint = gateway -> 7000 (websocket), 8080(http)

@Slf4j
public class CommonWebSocket {

   private static Map<WsContext, String> userUsernameMap = new ConcurrentHashMap<>();
   private static int nextUserNumber = 1; // Assign to username for next connecting user

   public void init () {
      Javalin app = Javalin.create(config -> {
         config.addStaticFiles("/templates");
      }).start(7000);

      app.ws("/chat", ws -> {
         ws.onConnect(ctx -> {
            String auth = ctx.queryParam("Authorized");
            log.debug("Authorized==" + auth);
            if (null == auth) {
               throw new RuntimeException("login need");
            }
            String username = "User" + nextUserNumber++;
            userUsernameMap.put(ctx, username);
            broadcastMessage("Server", (username + " joined the chat"));
         });
         ws.onClose(ctx -> {
            String username = userUsernameMap.get(ctx);
            userUsernameMap.remove(ctx);
            broadcastMessage("Server", (username + " left the chat"));
         });
         ws.onMessage(ctx -> {
            broadcastMessage(userUsernameMap.get(ctx), ctx.message());
         });
      });
   }

   // Sends a message from one user to all users, along with a list of current usernames
   private static void broadcastMessage(String sender, String message) {
      userUsernameMap.keySet().stream().filter(ctx -> ctx.session.isOpen()).forEach(session -> {
         session.send(
                 new JSONObject()
                         .put("userMessage", createHtmlMessageFromSender(sender, message))
                         .put("userlist", userUsernameMap.values()).toString()
         );
      });
   }

   // Builds a HTML element with a sender-name, a message, and a timestamp
   private static String createHtmlMessageFromSender(String sender, String message) {
      return article(
              b(sender + " says:"),
              span(attrs(".timestamp"), new SimpleDateFormat("HH:mm:ss").format(new Date())),
              p(message)
      ).render();
   }
}
