package com.sky.service;

import com.github.pagehelper.PageInfo;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.Orders;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrdersVO;

import java.util.List;

public interface OrderService {

    //用户下单
    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    //查询历史订单
    PageInfo<Orders> getHistory(Integer page, Integer pageSize, Integer status);

    //获取订单详情
    OrdersVO getDetail(Integer id);

    //根据订单id取消订单
    void cancelById(Integer id);

    //再来一单
    void repetition(Integer id);
}
