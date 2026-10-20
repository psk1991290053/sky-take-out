package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

    //营业额统计
    public TurnoverReportVO getTurnoverReport(LocalDate begin, LocalDate end) {
        //获得每天的日期，构造String
        List<LocalDate> dateList = new ArrayList();

        dateList.add(begin);
        while(!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        String dates = StringUtils.join(dateList, ",");

        //获得每天日期对应的营业额，构造String
        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            //获得具体时分秒 -> 和订单完成时间 checkoutTime 类型一致
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            //select sum(amount) from orders where checkout_time > beginTime and checkout_time < endTime and status = 5;
            Map map = new HashMap();
            map.put("begin",beginTime);
            map.put("end",endTime);
            map.put("status", Orders.COMPLETED);
            Double sum = orderMapper.sumByMap(map);
            sum = sum == null ? 0.0 : sum;
            turnoverList.add(sum);
        }

        String turnover = StringUtils.join(turnoverList, ",");

        //构造并返回VO对象
        return TurnoverReportVO.builder()
                .dateList(dates)
                .turnoverList(turnover)
                .build();
    }

    //用户数据统计
    public UserReportVO getUserReport(LocalDate begin, LocalDate end) {
        //准备dateList
        List<LocalDate> dateList = new ArrayList();

        dateList.add(begin);
        while(!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        //准备用户总量和每日新增用户数量
        List<Integer> totalUserList = new ArrayList<>();
        List<Integer> newUserList = new ArrayList<>();

        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map map = new HashMap();
            map.put("end",endTime);

            totalUserList.add(userMapper.getByMap(map));

            map.put("begin",beginTime);
            newUserList.add(userMapper.getByMap(map));
        }
        //封装结果并返回
        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .totalUserList(StringUtils.join(totalUserList,","))
                .newUserList(StringUtils.join(newUserList,","))
                .build();
    }

    //订单数据统计
    public OrderReportVO getOrdersReport(LocalDate begin, LocalDate end) {
        //获得日期列表
        List<LocalDate> dateList = new ArrayList();

        dateList.add(begin);
        while(!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        //获得每日订单总数列表和每日有效订单总数列表
        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();

        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Integer orderCount = countOrders(beginTime,endTime,null);
            Integer validOrderCount = countOrders(beginTime,endTime,Orders.COMPLETED);

            orderCountList.add(orderCount);
            validOrderCountList.add(validOrderCount);
        }

        //获得订单总数和有效订单总数
        Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();
        Integer validOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();

        //获得订单完成率
        Double orderCompletionRate = 0.0;
        if(totalOrderCount != 0){
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
        }

        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList,","))
                .orderCountList(StringUtils.join(orderCountList,","))
                .validOrderCountList(StringUtils.join(validOrderCountList,","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    //商品销量top10
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

        List<GoodsSalesDTO> goodsSalesDTOList = orderMapper.getSalesTop10(beginTime,endTime);
        List<String> names = new ArrayList<>();
        List<Integer> numbers = new ArrayList<>();

        for (GoodsSalesDTO item : goodsSalesDTOList) {
            names.add(item.getName());
            numbers.add(item.getNumber());
        }

        //转换成字符串类型
        String nameList = StringUtils.join(names, ",");
        String numberList = StringUtils.join(numbers, ",");

        return SalesTop10ReportVO.builder()
                .nameList(nameList)
                .numberList(numberList)
                .build();
    }

    private Integer countOrders(LocalDateTime begin,LocalDateTime end,Integer status){

        Map map = new HashMap();

        map.put("begin",begin);
        map.put("end",end);
        map.put("status",status);

        return orderMapper.countByMap(map);
    }

}
