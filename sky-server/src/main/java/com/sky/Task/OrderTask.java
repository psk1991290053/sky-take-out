package com.sky.Task;


import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    //超时未结算订单，15分钟即为超时
    @Scheduled(cron = "0 * * * * ?")//每分钟执行一次
    public void processTimeOutOrder(){
        log.info("订单超时，已取消: {}", LocalDateTime.now());
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        List<Orders> orders = orderMapper.getOrdersByStatusAndTimeLT(Orders.PENDING_PAYMENT,time);

        for (Orders order : orders) {
            order.setStatus(Orders.CANCELLED);
            order.setCancelTime(LocalDateTime.now());
            order.setCancelReason("订单长时间未支付");
            orderMapper.update(order);
        }
    }

    //每天凌晨一点清理一次仍显示派送中的订单，有的用户忘记点击送达
    @Scheduled(cron = "0 0 1 * * ?")
    public void processDeliveryOrder(){
        log.info("每日定时处理仍显示配送中的订单: {}",LocalDateTime.now());
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
        List<Orders> orders = orderMapper.getOrdersByStatusAndTimeLT(Orders.DELIVERY_IN_PROGRESS,time);

        for (Orders order : orders) {
            order.setStatus(Orders.COMPLETED);
            orderMapper.update(order);
        }
    }




}
