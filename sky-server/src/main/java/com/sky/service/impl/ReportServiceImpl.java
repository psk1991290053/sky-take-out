package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
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



}
