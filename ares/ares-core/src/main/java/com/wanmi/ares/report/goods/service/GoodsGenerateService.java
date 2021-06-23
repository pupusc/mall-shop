package com.wanmi.ares.report.goods.service;

import com.wanmi.ares.report.goods.dao.GoodsMapper;
import com.wanmi.ares.utils.Constants;
import com.wanmi.ares.utils.DateUtil;
import com.wanmi.ares.utils.GoodsDateUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-8-29
 * \* Time: 11:13
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@Service
public class GoodsGenerateService {

    public void generate(GoodsMapper mapper,int type){
        String date = GoodsDateUtil.getDay(type);
        String beginTime = date;
        String endTime = DateUtil.now();
        //先清理
        switch (type){
            case Constants.GoodsGenerateType.TODAY:
                endTime = DateUtil.nowTime();
                mapper.delDayReport(date);
                mapper.saveDayReportBySelect(date,beginTime,endTime);
                break;
            case Constants.GoodsGenerateType.YESTERDAY:
                mapper.delDayReport(date);
                mapper.saveDayReportBySelect(date,beginTime,endTime);
                break;
            case Constants.GoodsGenerateType.LATEST7DAYS:
                mapper.delSevenDayReport();
                mapper.saveSevenDayReportBySelect(beginTime,endTime);
                break;
            case Constants.GoodsGenerateType.LATEST30DAYS:
                mapper.delThirtyDayReport();
                mapper.saveThirtyDayReportBySelect(beginTime,endTime);
                break;
            case Constants.GoodsGenerateType.MONTH:
                LocalDate localDate = LocalDate.now();
                LocalDate beforeDate = localDate.minusMonths(1);
                beginTime = DateUtil.format(LocalDate.of(beforeDate.getYear(), beforeDate.getMonth(), 1),DateUtil.FMT_DATE_1);
                endTime = DateUtil.format(LocalDate.of(localDate.getYear(), localDate.getMonth(), 1),DateUtil.FMT_DATE_1);//本月第一天
                mapper.delMonthReport(Integer.parseInt(date));
                mapper.saveMonthReportBySelect(Integer.parseInt(date),beginTime,endTime);
                break;
        }

    }
}
