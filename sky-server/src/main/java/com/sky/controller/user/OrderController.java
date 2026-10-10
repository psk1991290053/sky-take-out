package com.sky.controller.user;


import com.github.pagehelper.PageInfo;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.Orders;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrdersVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    //用户下单
    @PostMapping("submit")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        log.info("用户下单，参数为: {}",ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submit(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        return Result.success(orderPaymentVO);
    }

    //查询历史订单
    @GetMapping("historyOrders")
    public Result<PageInfo<Orders>> getHistory(@RequestParam Integer page,
                                                     @RequestParam Integer pageSize,
                                                     @RequestParam Integer status){
        log.info("查询历史订单,page = {},pageSize = {},status = {}",page,pageSize,status);
        PageInfo<Orders> pageInfo = orderService.getHistory(page,pageSize,status);
        return Result.success(pageInfo);
    }

    //查询订单详情
    @GetMapping("/orderDetail/{id}")
    public Result<OrdersVO> getDetail(@PathVariable Integer id){
        log.info("查询订单详情: {}",id);
        OrdersVO ordersVO = orderService.getDetail(id);
        return Result.success(ordersVO);
    }

    //取消订单
    @PostMapping("/cancel/{id}")
    public Result cancel(@PathVariable Integer id){
        log.info("取消订单: {}",id);
        orderService.cancelById(id);
        return Result.success();
    }

    //再来一单
    @PostMapping("/repetition/{id}")
    public Result again(@PathVariable Integer id){
        log.info("再来一单: {}",id);
        orderService.repetition(id);
        return Result.success();
    }
}
