package com.post.springvertx.service;

import com.post.springvertx.entity.Transaction;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.ext.web.api.service.ServiceRequest;
import io.vertx.ext.web.api.service.ServiceResponse;

/**
 * 事务服务
 *
 * @author xieyingpan
 * @date 2023/05/10
 */

public interface TransactionService {
    /**
     * 把事务
     *
     * @param body          身体
     * @param context       上下文
     * @param resultHandler 结果处理程序
     */
    void putTransaction(Transaction body, ServiceRequest context, Handler<AsyncResult<ServiceResponse>> resultHandler);
}
