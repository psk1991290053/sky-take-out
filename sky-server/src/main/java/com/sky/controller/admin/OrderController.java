package com.sky.controller.admin;


import com.github.pagehelper.PageInfo;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersConfirmDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.entity.Orders;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrdersVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/admin/order")
@Slf4j
public class OrderController {

    @Autowired
    private OrderService orderService;

    //订单搜索
    @GetMapping("/conditionSearch")
    public Result<PageInfo<Orders>> search(@RequestParam Integer page,
                                           @RequestParam Integer pageSize,
                                           @RequestParam LocalDateTime beginTime,
                                           @RequestParam LocalDateTime endTime,
                                           @RequestParam String phone,
                                           @RequestParam Integer status,
                                           @RequestParam String number){
        log.info("订单搜索");
        //构建OrdersPageQueryDTO对象
        OrdersPageQueryDTO ordersPageQueryDTO = new OrdersPageQueryDTO();
        ordersPageQueryDTO.setPage(page);
        ordersPageQueryDTO.setPageSize(pageSize);
        ordersPageQueryDTO.setBeginTime(beginTime);
        ordersPageQueryDTO.setEndTime(endTime);
        ordersPageQueryDTO.setPhone(phone);
        ordersPageQueryDTO.setStatus(status);
        ordersPageQueryDTO.setNumber(number);
        ordersPageQueryDTO.setUserId(BaseContext.getCurrentId());
        //调用service层接口
        PageInfo<Orders> pageInfo = orderService.search(ordersPageQueryDTO);
        return Result.success(pageInfo);
    }

    //各个状态的订单数量统计
    @GetMapping("/statistics")
    public Result<OrderStatisticsVO> countByStatus(){
        log.info("各个状态的订单数量统计");
        OrderStatisticsVO orderStatisticsVO = orderService.countByStatus();
        return Result.success(orderStatisticsVO);
    }

    //查询订单详情
    @GetMapping("/details/{id}")
    public Result<OrdersVO> detail(@PathVariable Integer id){
        log.info("查询订单详情: {}",id);
        OrdersVO ordersVO = orderService.getDetail(id);
        return Result.success(ordersVO);
    }

    //接单
    @PutMapping("/confirm")
    public Result confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO){
        log.info("接单: {}",ordersConfirmDTO);
        orderService.confirm(ordersConfirmDTO.getId());
        return Result.success();
    }

   //拒单
    @PutMapping("/rejection")
    public Result rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO){
        log.info("拒单 :{}",ordersRejectionDTO);
        orderService.rejection(ordersRejectionDTO);
        return Result.success();
    }

    //拒单
    @PutMapping("/cancel")
    public Result cancel(@RequestBody OrdersCancelDTO ordersCancelDTO){
        log.info("拒单 :{}",ordersCancelDTO);
        orderService.cancel(ordersCancelDTO);
        return Result.success();
    }

    //派送订单
    @PutMapping("/delivery/{id}")
    public Result delivery(@PathVariable Long id){
        log.info("派送订单: {}",id);
        orderService.delivery(id);
        return Result.success();
    }

    //完成订单
    @PutMapping("/complete/{id}")
    public Result complete(@PathVariable Long id){
        log.info("完成订单: {}",id);
        orderService.complete(id);
        return Result.success();
    }
}
