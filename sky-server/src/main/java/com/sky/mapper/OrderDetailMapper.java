package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderDetailMapper {

    //插入n条订单详情数据
    void insertBatch(List<OrderDetail> batch);

    //根据订单id查询订单详情数据
    @Select("select * from order_detail where order_id = #{orderId}")
    List<OrderDetail> getByOrderId(Integer orderId);
}
