package com.post.springvertx.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 */
@Component
@Slf4j
public class SocketServerVerticle extends AbstractVerticle {
    @Value("${vertx.socket.server.port}")
    Integer port;

    @Override
    public void start() {
        NetServerOptions options = new NetServerOptions().setPort(port);
        NetServer server = vertx.createNetServer(options);
        server.connectHandler(socket -> {
            log.info("本地地址 : {},远程地址 : {}", socket.localAddress(), socket.remoteAddress());
            socket.handler(buffer -> {
                String trim = buffer.toString().trim();
                log.info("SocketServer接收 : {}", trim);
                socket.write(trim);
            });
        }).listen().onFailure(r -> log.error("SocketServer监听{}失败,原因 : {}", port, r.getMessage()));
    }
}
