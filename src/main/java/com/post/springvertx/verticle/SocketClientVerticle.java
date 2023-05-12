package com.post.springvertx.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 */
@Component
@Slf4j
public class SocketClientVerticle extends AbstractVerticle {
    @Value("${vertx.socket.client.port}")
    Integer port;
    @Value("${vertx.socket.client.host}")
    String host;

    @Override
    public void start() {
        NetClientOptions options = new NetClientOptions()
                .setConnectTimeout(10000)
                .setReconnectAttempts(10)
                .setReconnectInterval(500);
        NetClient client = vertx.createNetClient(options);

        client.connect(port, host).onSuccess(socket -> {
            log.info("本地地址 : {},远程地址 : {}", socket.localAddress(), socket.remoteAddress());
            socket.handler(buffer -> {
                String trim = buffer.toString().trim();
                log.info("SocketClient接收 : {}", trim);
            });
            socket.write("你好");
        }).onFailure(r -> log.error("SocketClient连接{}:{}失败,原因 : {}", host, port, r.getMessage()));
    }
}
