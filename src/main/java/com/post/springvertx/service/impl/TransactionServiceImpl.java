package com.post.springvertx.service.impl;

import com.post.springvertx.entity.Transaction;
import com.post.springvertx.service.TransactionService;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.web.api.service.ServiceRequest;
import io.vertx.ext.web.api.service.ServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 事务服务
 *
 * @author xieyingpan
 * @date 2023/05/10
 */
@Component
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    /**
     * 把事务
     *
     * @param body          身体
     * @param context       上下文
     * @param resultHandler 结果处理程序
     */
    @Override
    public void putTransaction(Transaction body, ServiceRequest context, Handler<AsyncResult<ServiceResponse>> resultHandler) {
        log.info(body.toString());
    }
}
