package com.post.springvertx.configuration;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.Map;


@Configuration
public class VertxConfig {
    @Bean
    public Vertx vertx() {
        VertxOptions options = new VertxOptions()
                .setEventLoopPoolSize(20)
                .setWorkerPoolSize(20);
        return Vertx.vertx(options);
    }

    @EventListener
    public void deployVerticles(ApplicationReadyEvent event) {
        //获取所有子类
        Map<String, AbstractVerticle> verticleMap = event.getApplicationContext().getBeansOfType(AbstractVerticle.class);
        Vertx vertx = event.getApplicationContext().getBean(Vertx.class);
        verticleMap.forEach((k, verticle) -> vertx.deployVerticle(verticle));
    }
}
