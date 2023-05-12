package com.post.springvertx.controller;

import com.post.springvertx.annotation.RequestMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

/**
 * 事务控制器
 *
 * @author xieyingpan
 * @date 2023/05/11
 */
@Controller
@Slf4j
public class TransactionController {

    @RequestMapping(value = "/123", method = "GET")
    public void hello() {
        log.info("1111111111111111");
    }
}
