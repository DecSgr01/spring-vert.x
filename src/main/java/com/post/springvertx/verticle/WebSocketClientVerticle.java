package com.post.springvertx.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 */
@Component
@Slf4j
@ConditionalOnProperty(prefix = "vertx.webSocket.client",name = {"port","host"})
public class WebSocketClientVerticle extends AbstractVerticle {
    @Value("${vertx.webSocket.client.port}")
    Integer port;
    @Value("${vertx.webSocket.client.host}")
    String host;

    @Override
    public void start() {
        HttpClientOptions httpClientOptions = new HttpClientOptions().setConnectTimeout(100);

        HttpClient httpClient = vertx.createHttpClient(httpClientOptions);
        httpClient.webSocket(port, host, "/ws").onSuccess(webSocket -> {
            log.info("本地地址 : {},远程地址 : {}", webSocket.localAddress(), webSocket.remoteAddress());
            webSocket.handler(buffer -> {
                String trim = buffer.toString().trim();
                log.info("WebsocketClient接收 : {}", trim);
            });
            webSocket.writeTextMessage("你好");
        }).onFailure(r -> log.error("WebsocketClient连接{}:{}失败,原因 : {}", host, port, r.getMessage()));
    }
}
