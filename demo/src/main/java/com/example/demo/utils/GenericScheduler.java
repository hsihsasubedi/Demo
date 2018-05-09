package com.example.demo.utils;


import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class GenericScheduler {

    @Autowired
    private OrderService orderService;

    @Scheduled(cron = DemoConstants.SCHEDULER_CRON)
    public void orderProcessing() {
        DemoLogger.info("----- Scheduler Running ----- :: " + Instant.now());
        DemoLogger.info(orderService.updateProcessedOrderToCompleted() + " rows updated.");
    }
}
