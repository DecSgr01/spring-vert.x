package com.post.springvertx.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 */
@Component
@Slf4j
@ConditionalOnProperty(prefix = "vertx.websocket.server",name = "port")
public class WebSocketServerVerticle extends AbstractVerticle {
    @Value("${vertx.websocket.server.port}")
    Integer port;

    @Override
    public void start() {
        HttpServer httpServer = vertx.createHttpServer();

        httpServer.webSocketHandler(webSocket -> {
            log.info("本地地址 : {},远程地址 : {},uri : {}", webSocket.localAddress(), webSocket.remoteAddress(), webSocket.uri());
            webSocket.handler(buffer -> {
                String trim = buffer.toString().trim();
                log.info("WebsocketServer接收 : {}", trim);
                webSocket.writeTextMessage(trim);
            });
        }).listen(port).onFailure(r -> log.error("WebsocketServer监听{}失败,原因 : {}", port, r.getMessage()));
    }
}
