package com.post.springvertx.verticle;

import com.post.springvertx.annotation.RequestMapping;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.lang.annotation.Annotation;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Administrator
 */
@Component
@Slf4j
public class HttpServerVerticle extends AbstractVerticle {
    @Value("${vertx.http.server.port}")
    Integer port;
    Router router = Router.router(vertx);

    @Override
    public void start() {
        HttpServer httpServer = vertx.createHttpServer();
        // 创建路由对象
        router.errorHandler(500, rc -> {
            Throwable failure = rc.failure();
            if (failure != null) {
                log.error("[Router Error Handler]", failure);
            }
        });
        // 把请求交给路由处理--------------------(1)
        httpServer.requestHandler(router).listen(port).onFailure(r -> log.error("HttpServer监听{}失败,原因 : {}", port, r.getMessage()));
    }

    @EventListener
    public void route(ApplicationReadyEvent event) {
        ConfigurableApplicationContext applicationContext = event.getApplicationContext();
        Map<String, Object> handlers = applicationContext.getBeansWithAnnotation(Controller.class);
        for (Map.Entry<String, Object> entry : handlers.entrySet()) {
            Object value = entry.getValue();
            for (Method method : value.getClass().getMethods()) {
                Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
                log.debug(method.getName());
                for (Annotation ann : declaredAnnotations) {
                    if (ann.annotationType().getName().equals(RequestMapping.class.getName())) {
                        String requestMappingValue = method.getAnnotation(RequestMapping.class).value();
                        String requestMappingMethod = method.getAnnotation(RequestMapping.class).method();
                        router.route(requestMappingValue).method(HttpMethod.valueOf(requestMappingMethod.toUpperCase())).handler(h -> {
                            try {
                                Object result = MethodHandles.lookup().unreflect(method).bindTo(value).invoke();
                                HttpServerResponse response = h.response();
                                if (!response.headWritten()) {
                                    response.putHeader(HttpHeaders.CONTENT_TYPE, "application/json;charset=UTF-8");
                                }
                                // Write to the response and end it
                                Consumer<Object> responseEnd = x -> {
                                    if (method.getReturnType() == void.class) {
                                        response.end();
                                    } else {
                                        response.end(x instanceof CharSequence ? x.toString() : Json.encode(x));
                                    }
                                };
                                responseEnd.accept(result);
                            } catch (Throwable throwable) {
                                log.error("request error, {}::{}", value.getClass().getName(), method.getName(), throwable);
                                Map<String, Object> map = new HashMap<>(1);
                                map.put("message", "system error");
                                h.response().end(Json.encode(map));
                            }
                        });
                    }
                }
            }
        }
    }
}
