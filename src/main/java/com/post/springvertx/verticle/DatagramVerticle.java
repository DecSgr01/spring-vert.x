package com.post.springvertx.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.datagram.DatagramSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * udp
 *
 * @author xyp
 */
@Slf4j
@Component
public class DatagramVerticle extends AbstractVerticle {
    @Value("${vertx.datagram.port}")
    Integer port;

    @Override
    public void start() {
        DatagramSocket datagramSocket = vertx.createDatagramSocket();
        datagramSocket.handler(packet -> log.info("datagramSocket 接收: {}", packet.data()))
                .listen(port, "0.0.0.0")
                .onFailure(r -> log.error("DatagramSocket监听{}失败,原因 : {}", port, r.getMessage()));

        Buffer buffer = Buffer.buffer("你好");
        // Send a Buffer
        datagramSocket.send(buffer, port, "127.0.0.1").onComplete(asyncResult -> log.info("Send {} ", asyncResult.succeeded()));
    }
}
