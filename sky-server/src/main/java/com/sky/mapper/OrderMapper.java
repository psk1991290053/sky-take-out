package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface OrderMapper {

    //用户下单
    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    //根据userId获取历史订单信息
    @Select("select * from orders where user_id = #{userId}")
    List<Orders> getOrdersByUserId(Long userId);

    //根据订单id获取订单数据
    @Select("select * from orders where id = #{id}")
    Orders getById(Integer id);

    //根据订单id取消订单
    @Update("update orders set status = 6 where id = #{id}")
    void cancelById(Integer id);
}
