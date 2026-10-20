package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import java.time.LocalDate;

public interface ReportService {

    //营业额统计
    TurnoverReportVO getTurnoverReport(LocalDate begin, LocalDate end);

    //用户数据统计
    UserReportVO getUserReport(LocalDate begin, LocalDate end);

    //订单数据统计
    OrderReportVO getOrdersReport(LocalDate begin, LocalDate end);

    //商品销量top10
    SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end);
}
